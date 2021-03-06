/*
 * $RCSfile: MlibNotOpImage.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005-02-11 04:56:02 $
 * $State: Exp $
 */
package com.sun.media.jai.mlib;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import javax.media.jai.ImageLayout;
import javax.media.jai.OpImage;
import javax.media.jai.PointOpImage;
import java.util.Map;
import com.sun.medialib.mlib.*;
// import com.sun.media.jai.test.OpImageTester;

/**
 * An OpImage that performs the Not operation on 2 images through mediaLib.
 *
 */
final class MlibNotOpImage extends PointOpImage {

    /**
     * Constructs an MlibNotOpImage. The image dimensions are copied
     * from the source image.  The tile grid layout, SampleModel, and
     * ColorModel may optionally be specified by an ImageLayout object.
     *
     * @param source    a RenderedImage.
     * @param layout    an ImageLayout optionally containing the tile
     *                  grid layout, SampleModel, and ColorModel, or null.
     */
    public MlibNotOpImage(RenderedImage source,
                          Map config,
                          ImageLayout layout) {
	super(source, layout, config, true);
        // Set flag to permit in-place operation.
        permitInPlaceOperation();
    }

    /**
     * Not the pixel values of a rectangle from the source.
     * The source is cobbled.
     *
     * @param sources   an array of sources, guarantee to provide all
     *                  necessary source data for computing the rectangle.
     * @param dest      a tile that contains the rectangle to be computed.
     * @param destRect  the rectangle within this OpImage to be processed.
     */
    protected void computeRect(Raster[] sources,
                               WritableRaster dest,
                               Rectangle destRect) {

        int formatTag = MediaLibAccessor.findCompatibleTag(sources,dest);

        MediaLibAccessor srcAccessor = new MediaLibAccessor(sources[0],
							    destRect,formatTag);
        MediaLibAccessor dstAccessor = new MediaLibAccessor(dest,
							    destRect, formatTag);

        switch (dstAccessor.getDataType()) {
        case DataBuffer.TYPE_BYTE:
        case DataBuffer.TYPE_USHORT:
        case DataBuffer.TYPE_SHORT:
        case DataBuffer.TYPE_INT:
            mediaLibImage[] srcML = srcAccessor.getMediaLibImages();
            mediaLibImage[] dstML = dstAccessor.getMediaLibImages();
            for (int i = 0 ; i < dstML.length; i++) {
                Image.Not(dstML[i], srcML[i]);
            }
            break;
        default:
            String className = this.getClass().getName();
            throw new RuntimeException(className + JaiI18N.getString("Generic2"));
        }

        if (dstAccessor.isDataCopy()) {
            dstAccessor.clampDataArrays();
            dstAccessor.copyDataToRaster();
        }
    }

//     public static void main (String args[]) {
//         System.out.println("MlibNotOpImage Test");
//         ImageLayout layout;
//         OpImage src, dst;
//         Rectangle rect = new Rectangle(0, 0, 5, 5);

//         System.out.println("1. PixelInterleaved byte 3-band");
//         layout = OpImageTester.createImageLayout(0, 0, 800, 800, 0, 0,
// 						 200, 200, DataBuffer.TYPE_BYTE,
// 						 3, false);
//         src = OpImageTester.createRandomOpImage(layout);
//         dst = new MlibNotOpImage(src, null, null);
//         OpImageTester.testOpImage(dst, rect);
//         OpImageTester.timeOpImage(dst, 10);

//         System.out.println("2. Banded byte 3-band");
//         layout = OpImageTester.createImageLayout(0, 0, 800, 800, 0, 0,
// 						 200, 200, DataBuffer.TYPE_BYTE,
// 						 3, true);
//         src = OpImageTester.createRandomOpImage(layout);
//         dst = new MlibNotOpImage(src, null, null);
//         OpImageTester.testOpImage(dst, rect);
//         OpImageTester.timeOpImage(dst, 10);

//         System.out.println("3. PixelInterleaved int 3-band");
//         layout = OpImageTester.createImageLayout(0, 0, 512, 512, 0, 0, 200, 200,
// 						 DataBuffer.TYPE_INT, 3, false);
//         src = OpImageTester.createRandomOpImage(layout);
//         dst = new MlibNotOpImage(src, null, null);
//         OpImageTester.testOpImage(dst, rect);
//         OpImageTester.timeOpImage(dst, 10);

//         System.out.println("4. Banded int 3-band");
//         layout = OpImageTester.createImageLayout(0, 0, 512, 512, 0, 0,
// 						 200, 200, DataBuffer.TYPE_INT,
// 						 3, true);
//         src = OpImageTester.createRandomOpImage(layout);
//         dst = new MlibNotOpImage(src, null, null);
//         OpImageTester.testOpImage(dst, rect);
//         OpImageTester.timeOpImage(dst, 10);
//     }
}
