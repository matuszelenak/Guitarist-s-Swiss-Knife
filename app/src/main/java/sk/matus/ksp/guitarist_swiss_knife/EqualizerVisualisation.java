package sk.matus.ksp.guitarist_swiss_knife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Arrays;

/**
 * A kind of tuner visualization that shows the spread of frequencies and their intensity
 * on a 2-dimensional plane
 */
public class EqualizerVisualisation extends LinearLayout implements TunerVisualisation{
    Context context;
    private TextureView mTextureView;
    private RenderThread mThread;
    private int mWidth;
    private int mHeight;
    /**
     * A variable that holds the dimensions of the SurfaceView canvas so that it
     * doesn't have to be queried each time.
     */
    private Paint wavePaint = new Paint();
    private Paint freqPaint = new Paint();
    private int backgroundColor;
    /**
     * An array containing the current block of samples processed by FFT.
     */
    private double[] freqData;
    /**
     * Self-descriptive, contains the current frequency taken from freqData
     */
    private double currentFreq = 0;
    /**
     * Contains the string representation of the tone which corresponds to currentFreq
     */
    private Tone currentTone;
    /**
     * Contains the information about the currentTone being either higherSemitone, lowerSemitone or precisely at currentFreq.
     */
    private String currentDirection = "";

    public EqualizerVisualisation(Context context){
        super(context);
        this.context = context;
        wavePaint.setColor(context.getResources().getColor(R.color.colorKSPGreen));
        freqPaint.setColor(context.getResources().getColor(R.color.colorKSPGreen));
        freqPaint.setTextSize(40);
        backgroundColor = context.getResources().getColor(R.color.colorActivityBackground);
        mTextureView = new TextureView(context);
        mTextureView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mTextureView.setSurfaceTextureListener(new CanvasListener());
        mTextureView.setOpaque(false);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(mTextureView);
    }

    /**
     * Method that updates the content of the canvas.
     * @param canvas A canvas to be drawn to*/
    public void doDraw(Canvas canvas) {
        if ((canvas==null) || (freqData==null)) return;
        canvas.drawColor(backgroundColor);
        drawEqualizer(canvas);
        drawEstimation(canvas);
    }

    /**
     * Method draws the visual representation of the recorded sound.
     * The visualisation is a graph where the y-axis is logarithmically scaled amplitude and x-axis is
     * the frequency of the sound.
     * @param canvas The canvas to be drawn to*/
    private void drawEqualizer(Canvas canvas){
        int baseLineY = (int)(mHeight*0.8);
        for (int i = 0, x=10;  i < freqData.length; i+=freqData.length/(mWidth-20), x++){
            int amplitudePeak = (int)(baseLineY - Math.max(4/baseLineY,Math.log(freqData[i]*10000))*baseLineY/4);
            canvas.drawLine(x,baseLineY,x,amplitudePeak,wavePaint);
        }
    }

    /**
     * Method draws the current prevalent frequency and a tone estimation for it.
     * @param canvas The canvas to be drawn to*/
    private void drawEstimation(Canvas canvas){
        int x = (int)(mWidth*0.1);
        canvas.drawText(String.format("Current frequency is %.2f Hz",currentFreq),x,(int)(mHeight*0.05), freqPaint);
        canvas.drawText(String.format("%s %s", currentDirection, currentTone), x, (int) (mHeight * 0.05) + freqPaint.getTextSize() + 5, freqPaint);
    }

    /**
     * Method updates its current freqData with a new block.
     * @param data the new block of data to use
     */
    public void updateSamples(double[] data){
        this.freqData = Arrays.copyOf(data, data.length);
    }

    /**
     * Method updates its currenFreq with a new value.
     * @param freq the new frequency to use.
     */
    public void updateMaxFrequency(double freq){
        this.currentFreq = freq;
    }

    /**
     * Method that updates the currentTone and currentDirection with up-to-date data.
     * @param tone
     */

    public void updateTone(Tone tone){
        currentTone = tone;
        double error = currentFreq - tone.getFrequency();
        double lowerLimit = (tone.getFrequencyInterval().x - tone.getFrequency())/2;
        double upperLimit = (tone.getFrequencyInterval().y - tone.getFrequency())/2;
        if (error > 0){
            currentDirection = context.getString(R.string.frequency_below);
            if (error < upperLimit) currentDirection = context.getString(R.string.frequency_precise);
        } else
        {
            currentDirection = context.getString(R.string.frequency_above);
            if (error > lowerLimit) currentDirection = context.getString(R.string.frequency_precise);
        }
    }

    private class RenderThread extends Thread {
        private volatile boolean mRunning = true;

        @Override
        public void run() {

            while (mRunning && !Thread.interrupted()) {
                final Canvas canvas = mTextureView.lockCanvas(null);
                try {
                    doDraw(canvas);
                } finally {
                    mTextureView.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopRendering() {
            interrupt();
            mRunning = false;
        }

    }

    private class CanvasListener implements SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface,
                                              int width, int height) {
            mThread = new RenderThread();
            mThread.start();
            mWidth = mTextureView.getWidth();
            mHeight = mTextureView.getHeight();
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            if (mThread != null) {
                mThread.stopRendering();
            }
            return true;
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
                                                int width, int height) {
            mWidth = mTextureView.getWidth();
            mHeight = mTextureView.getHeight();
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    }
}
