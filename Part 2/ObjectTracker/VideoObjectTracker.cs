using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Accord.Video.FFMPEG;

namespace ObjectTracker
{
    public class VideoObjectTracker
    {
        public void Process(string inputPath, string outputDirectory, string outputName)
        {
            Directory.CreateDirectory(outputDirectory);

            using (var reader = new VideoFrameReader(inputPath))
            {
                using (var writer = new VideoFileWriter())
                {
                    writer.Open(
                        outputDirectory + outputName,
                        reader.UnderlyingReader.Width,
                        reader.UnderlyingReader.Height
                    );

                    Bitmap lastBitmap = null;
                    reader.ReadAllFrames(
                        (bitmap, index) =>
                        {
                            ProcessFrame(bitmap, lastBitmap);
                            lastBitmap?.Dispose();
                            writer.WriteVideoFrame(bitmap);
                            lastBitmap = bitmap;
                        }
                    );
                    lastBitmap.Dispose();
                }
            }
        }

        private void ProcessFrame(Bitmap bitmap, Bitmap lastBitmap)
        {
            if (lastBitmap != null)
            {
                bitmap.SetPixel(0, 0, Color.Red);
            }
        }
    }
}
