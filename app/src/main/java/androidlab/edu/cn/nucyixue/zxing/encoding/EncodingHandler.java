package androidlab.edu.cn.nucyixue.zxing.encoding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

/**
 * @author Ryan Tang
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	
	public static Bitmap createQRCode(String str,int widthAndHeight){
		try {
			// �ж�URL�Ϸ���
			if (str == null || "".equals(str) || str.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
	        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
	        // ͼ������ת����ʹ���˾���ת��
			BitMatrix matrix = new MultiFormatWriter().encode(str,
					BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];
			// �������ﰴ�ն�ά����㷨��������ɶ�ά���ͼƬ��
			// ����forѭ����ͼƬ����ɨ��Ľ��
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = BLACK;
					}
				}
			}
			// ���ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * ����������
	 * 
	 * @param context
	 * @param contents
	 *            ��Ҫ���ɵ�����
	 * @param desiredWidth
	 *            ����������Ŀ��
	 * @param desiredHeight
	 *            ����������ĸ߶�
	 * @param displayCode
	 *            �Ƿ����������·���ʾ����
	 * @return
	 */
	public static Bitmap creatBarcode(Context context, String contents,int desiredWidth, int desiredHeight, boolean displayCode) {
		try {
			Bitmap ruseltBitmap = null;
			/**
			 * ͼƬ�����������Ŀհ׵Ŀ��
			 */
			int marginW = 20;
			/**
			 * ������ı�������
			 */
			BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

			if (displayCode) {
				Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
						desiredWidth, desiredHeight);
				Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2
						* marginW, desiredHeight, context);
				ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
						0, desiredHeight));
			} else {
				ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
						desiredWidth, desiredHeight);
			}

			return ruseltBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �����������Bitmap
	 * 
	 * @param contents
	 *            ��Ҫ���ɵ�����
	 * @param format
	 *            �����ʽ
	 * @param desiredWidth
	 * @param desiredHeight
	 * @return
	 * @throws WriterException
	 */
	protected static Bitmap encodeAsBitmap(String contents,BarcodeFormat format, int desiredWidth, int desiredHeight) {
		final int WHITE = 0xFFFFFFFF;
		final int BLACK = 0xFF000000;

		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = null;
		try {
			result = writer.encode(contents, format, desiredWidth,
					desiredHeight, null);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * ������ʾ�����Bitmap
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param context
	 * @return
	 */
	protected static Bitmap creatCodeBitmap(String contents, int width,int height, Context context) {
		TextView tv = new TextView(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(layoutParams);
		tv.setText(contents);
		tv.setHeight(height);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setWidth(width);
		tv.setDrawingCacheEnabled(true);
		tv.setTextColor(Color.BLACK);
		tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

		tv.buildDrawingCache();
		Bitmap bitmapCode = tv.getDrawingCache();
		return bitmapCode;
	}

	/**
	 * ������Bitmap�ϲ���һ��
	 * 
	 * @param first
	 * @param second
	 * @param fromPoint
	 *            �ڶ���Bitmap��ʼ���Ƶ���ʼλ�ã�����ڵ�һ��Bitmap��
	 * @return
	 */
	protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		int marginW = 20;
		Bitmap newBitmap = Bitmap.createBitmap(
				first.getWidth() + second.getWidth() + marginW,
				first.getHeight() + second.getHeight(), Config.ARGB_4444);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, marginW, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();

		return newBitmap;
	}
}
