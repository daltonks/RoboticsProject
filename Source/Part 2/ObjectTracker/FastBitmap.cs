using System;
using System.Drawing;
using System.Drawing.Imaging;

namespace ObjectTracker
{
    public unsafe class FastBitmap
    {
        public Bitmap Bitmap { get; private set; }

        // three elements used for MakeGreyUnsafe
        int dataWidth;
        BitmapData bitmapData = null;
        Byte* pBase = null;
        PixelData[,] pixels;
        public int Width { get; private set; }
        public int Height { get; private set; }

        public FastBitmap(Bitmap bitmap)
        {
            if (bitmap != null)
            {
                Bitmap = bitmap.Clone() as Bitmap;
            }
        }

        public void Dispose()
        {
            Bitmap.Dispose();
        }

        private Point PixelSize
        {
            get
            {
                GraphicsUnit unit = GraphicsUnit.Pixel;
                RectangleF bounds = Bitmap.GetBounds(ref unit);

                return new Point((int)bounds.Width, (int)bounds.Height);
            }
        }

        public void LockBitmap()
        {
            GraphicsUnit unit = GraphicsUnit.Pixel;
            RectangleF boundsF = Bitmap.GetBounds(ref unit);
            Width = (int) boundsF.Width;
            Height = (int) boundsF.Height;
            Rectangle bounds = new Rectangle((int)boundsF.X,
                (int)boundsF.Y,
                (int)boundsF.Width,
                (int)boundsF.Height);
            
            // Figure out the number of bytes in a row
            // This is rounded up to be a multiple of 4
            // bytes, since a scan line in an image must always be a multiple of 4 bytes
            // in length. 
            dataWidth = (int)boundsF.Width * sizeof(PixelData);
            if (dataWidth % 4 != 0)
            {
                dataWidth = 4 * (dataWidth / 4 + 1);
            }
            bitmapData = Bitmap.LockBits(bounds, ImageLockMode.ReadWrite, PixelFormat.Format24bppRgb);

            pBase = (Byte*)bitmapData.Scan0.ToPointer();

            pixels = new PixelData[Bitmap.Width, Bitmap.Height];
            for (var x = 0; x < Width; x++)
            {
                for (var y = 0; y < Height; y++)
                {
                    pixels[x, y] = *PixelAt(x, y);
                }
            }
        }

        public PixelData GetPixel(int x, int y)
        {
            return pixels[x, y];
        }

        public void SetPixel(int x, int y, PixelData colour)
        {
            PixelData* pixel = PixelAt(x, y);
            *pixel = colour;
        }

        public void UnlockBitmap()
        {
            Bitmap.UnlockBits(bitmapData);
            bitmapData = null;
            pBase = null;
        }

        public PixelData* PixelAt(int x, int y)
        {
            return (PixelData*)(pBase + y * dataWidth + x * sizeof(PixelData));
        }
    }
    public struct PixelData
    {
        public byte blue;
        public byte green;
        public byte red;
    }
}
