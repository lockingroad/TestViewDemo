package com.example.zhihu.testviewdemo.ebookdownload;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhihu.testviewdemo.DisplayUtils;
import com.example.zhihu.testviewdemo.R;


/**
 * @author xlrei @ Zhihu Inc.
 * @author apollo.yu @Zhihu Inc.
 * @since 08-09-2016
 */
public class EBookDownloadSimpleButton extends RelativeLayout {

    public static final int STATUS_NOT_START = 0x001;

    public static final int STATUS_DOWNLOADING = 0x002;

    public static final int STATUS_STOP = 0x003;

    public static final int STATUS_FAILED = 0x004;

    public static final int STATUS_FINISHED = 0x005;

    protected int mStatus = STATUS_NOT_START;

    private int mDownloadProgress;

    private DownloadStatusInterface mDownloadStatusInterface;

    private Paint mRingPaint;
    private Paint mCirclePaint;
    private Paint mPlayPaint;

    private RectF mRingRectF;
    private RectF mCircleRectF;
    private RectF mPlayRectF;

    private ImageView mFinishImg;
    private TextView mStartTv;


    @IntDef({STATUS_FAILED, STATUS_NOT_START, STATUS_STOP, STATUS_FINISHED, STATUS_DOWNLOADING})
    public @interface DownloadStatus {
    }

    public EBookDownloadSimpleButton(Context paramContext) {
        super(paramContext);
        init(paramContext);
    }

    public EBookDownloadSimpleButton(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    public EBookDownloadSimpleButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init(paramContext);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_ebook_donwload_simple_button, this, true);
        mFinishImg = findViewById(R.id.font_download_finish);
        mStartTv = findViewById(R.id.font_download_start);
        setGravity(Gravity.CENTER);
        setWillNotDraw(false);
        setOnClickListener(l -> {
            updateStatus(mStatus);
            if (mOnClickListener != null && mStatus == STATUS_FINISHED) {
                mOnClickListener.onFinishStatusButtonClicked();
            }
        });

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mStatus == STATUS_DOWNLOADING || mStatus == STATUS_STOP) {
            int centerY = getHeight() / 2;
            int centerX = getWidth() / 2;
            int ringWidth = DisplayUtils.dpToPixel(getContext(), 7.5f);
            int circleWidth = DisplayUtils.dpToPixel(getContext(), 1f);

            int padding = DisplayUtils.dpToPixel(getContext(), 4);
            int radius = getHeight()/2 - padding;
            int playSide = DisplayUtils.dpToPixel(getContext(), 3.5f);

            if (mRingRectF == null) {
                mRingRectF = new RectF(
                        centerX - radius,
                        centerY - radius,
                        centerX + radius,
                        centerY + radius);
            }

            if (mCircleRectF == null) {
                mCircleRectF = new RectF(
                        centerX - (radius+ringWidth/2),
                        centerY - (radius+ringWidth/2),
                        centerX + (radius+ringWidth/2),
                        centerY + (radius+ringWidth/2));
            }

            if (mPlayRectF == null) {
                mPlayRectF = new RectF(
                        centerX - playSide,
                        centerY - playSide,
                        centerX + playSide,
                        centerY + playSide);
            }

            if (mRingPaint == null) {
                mRingPaint = new Paint();
                mRingPaint.setAntiAlias(true);
                mRingPaint.setStrokeWidth(ringWidth);
                mRingPaint.setStyle(Paint.Style.STROKE);
                mRingPaint.setColor(Color.GREEN);
            }
            if (mCirclePaint == null) {
                mCirclePaint = new Paint();
                mCirclePaint.setAntiAlias(true);
                mCirclePaint.setStrokeWidth(circleWidth);
                mCirclePaint.setStyle(Paint.Style.STROKE);
                mCirclePaint.setColor(Color.GREEN);
            }
            if (mPlayPaint == null) {
                mPlayPaint = new Paint();
                mPlayPaint.setStyle(Paint.Style.FILL);
                mPlayPaint.setAntiAlias(true);
                mPlayPaint.setColor(Color.GREEN);
            }

            float sweepAngle = mDownloadProgress * 3.6f;
            canvas.drawArc(mRingRectF, -90, sweepAngle, false, mRingPaint);
            canvas.drawArc(mCircleRectF, 0, 360,false, mCirclePaint);
//            canvas.drawRoundRect(mPlayRectF, 7,7, mPlayPaint);

            Path path = new Path();
            path.moveTo((float) (centerX - playSide * Math.cos(Math.PI / 3)), (float) (centerY - playSide * Math.cos(
                    Math.PI / 6)));
            path.lineTo(centerX + playSide, centerY);
            path.lineTo((float) (centerX - playSide * Math.cos(Math.PI / 3)), (float) (centerY + playSide * Math.cos(
                    Math.PI / 6)));
            path.close();
            canvas.drawPath(path, mPlayPaint);

        }
    }

    public void setDownloadStatusListener(final DownloadStatusInterface downloadStatusInterface) {
        this.mDownloadStatusInterface = downloadStatusInterface;
    }

    public void setDownloadProgress(final int progress, final boolean needAnim) {
        if (mStatus != STATUS_DOWNLOADING && mStatus != STATUS_STOP) {
            return;
        }

        if (progress < 0 || progress > 100) {
            return;
        }

        mDownloadProgress = progress;

        invalidate();
    }

    public void setStatus(@DownloadStatus int status) {
        mStatus = status;

        if (STATUS_NOT_START == status) {
            mDownloadProgress = 0;
        } else if (STATUS_FINISHED == status ) {
            mFinishImg.setVisibility(VISIBLE);
            mStartTv.setVisibility(INVISIBLE);
        } else if (STATUS_FAILED == status ) {
            mFinishImg.setVisibility(INVISIBLE);
            mStartTv.setVisibility(VISIBLE);
        } else if (STATUS_DOWNLOADING == status||  STATUS_STOP == status) {
            mFinishImg.setVisibility(INVISIBLE);
//            mStartTv.setVisibility(INVISIBLE);
        }

        invalidate();
    }

    private void updateStatus(@DownloadStatus int status) {
        mStatus = status;

        switch (mStatus) {
            case STATUS_NOT_START:
            case STATUS_FAILED:
                mStatus = STATUS_DOWNLOADING;
                if (mDownloadStatusInterface != null) {
                    mDownloadStatusInterface.startDownload();
                }
                break;
            case STATUS_DOWNLOADING:
                mStatus = STATUS_STOP;
                if (mDownloadStatusInterface != null) {
                    mDownloadStatusInterface.stopDownload();
                }
                break;
            case STATUS_STOP:
                mStatus = STATUS_DOWNLOADING;
                if (mDownloadStatusInterface != null) {
                    mDownloadStatusInterface.continueDownload();
                }
                break;
            default:
                break;
        }

        setStatus(mStatus);
    }

    public int getStatus() {
        return mStatus;
    }

    public interface DownloadStatusInterface {

        void startDownload();

        void stopDownload();

        void continueDownload();
    }

    private OnBtnClickListener mOnClickListener;

    public void setOnBtnClickListener(OnBtnClickListener l) {
        mOnClickListener = l;
    }

    public interface OnBtnClickListener {
        void onFinishStatusButtonClicked();
    }
}
