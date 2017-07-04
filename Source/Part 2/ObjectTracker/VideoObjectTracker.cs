using System;
using System.Collections.Concurrent;
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
        private readonly int DIFFERENCE_RADIUS = 3;
        private readonly double MOVEMENT_STANDARD_DEVIATIONS = 8.5;

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
                    
                    var tasks = new List<Task<Bitmap>>();
                    Bitmap lastBitmap = null;
                    reader.ReadAllFrames(
                        (bitmap, index) =>
                        {
                            //if (index > 200)
                            //{
                            //    return;
                            //}
                            var fastBitmap = new FastBitmap(bitmap);
                            var fastLastBitmap = lastBitmap == null ? null : new FastBitmap(lastBitmap);
                            lastBitmap = bitmap;
                            tasks.Add(Task.Run(
                                () =>
                                {
                                    fastBitmap.LockBitmap();
                                    fastLastBitmap?.LockBitmap();
                                    var outputBitmap = ProcessFrame(fastBitmap, fastLastBitmap);
                                    fastBitmap.UnlockBitmap();
                                    fastLastBitmap?.UnlockBitmap();
                                    fastLastBitmap?.Dispose();
                                    return outputBitmap;
                                }
                            ));
                        }
                    );

                    for (int i = 0; i < tasks.Count(); i++)
                    {
                        var task = tasks[i];
                        writer.WriteVideoFrame(task.Result);
                        Console.WriteLine($"Processed frame {i}");
                    }
                }
            }
        }

        private Bitmap ProcessFrame(FastBitmap bitmap, FastBitmap lastBitmap)
        {
            if (lastBitmap == null)
            {
                return bitmap.Bitmap;
            }

            var colorDifferenceMatrix = new int[bitmap.Width, bitmap.Height];
            long totalColorDifferences = 0;
            for (var x = 0; x < bitmap.Width; x++)
            {
                for (var y = 0; y < bitmap.Height; y++)
                {
                    int difference = 0;
                    for (var xOffset = -DIFFERENCE_RADIUS; xOffset <= DIFFERENCE_RADIUS; xOffset++)
                    {
                        for (var yOffset = -DIFFERENCE_RADIUS; yOffset <= DIFFERENCE_RADIUS; yOffset++)
                        {
                            var xOffsetSquared = xOffset * xOffset;
                            var yOffsetSquared = yOffset * yOffset;
                            var distance = Math.Sqrt(xOffsetSquared - yOffsetSquared);
                            if (distance <= DIFFERENCE_RADIUS)
                            {
                                difference += GetPixelDifference(bitmap, lastBitmap, x + xOffset, y + yOffset);
                            }
                        }
                    }
                        
                    colorDifferenceMatrix[x, y] = difference;
                    totalColorDifferences += difference;
                }
            }

            double meanDifference = (double) totalColorDifferences / (bitmap.Width * bitmap.Height);
            double sumOfSquaredMeanDifferences = 0;
            for (var x = 0; x < colorDifferenceMatrix.GetLength(0); x++)
            {
                for (var y = 0; y < colorDifferenceMatrix.GetLength(1); y++)
                {
                    var dif = colorDifferenceMatrix[x, y] - meanDifference;
                    sumOfSquaredMeanDifferences += dif * dif;
                }
            }

            double standardDeviation = Math.Sqrt(1d / (bitmap.Width * bitmap.Height) * sumOfSquaredMeanDifferences);
            
            for (var x = 0; x < colorDifferenceMatrix.GetLength(0); x++)
            {
                for (var y = 0; y < colorDifferenceMatrix.GetLength(1); y++)
                {
                    var colorDifference = colorDifferenceMatrix[x, y];
                    if (colorDifference >= standardDeviation * MOVEMENT_STANDARD_DEVIATIONS)
                    {
                        bitmap.SetPixel(x, y, new PixelData{ red = 127, green = 255, blue = 0 });
                    }
                }
            }
            return bitmap.Bitmap;
        }

        private int GetPixelDifference(FastBitmap bitmap, FastBitmap lastBitmap, int x, int y)
        {
            if (x < 0 || y < 0 || x >= bitmap.Width || y >= bitmap.Height)
            {
                return 0;
            }

            var color = bitmap.GetPixel(x, y);
            var lastColor = lastBitmap.GetPixel(x, y);
            return Math.Abs(color.red - lastColor.red)
                             + Math.Abs(color.green - lastColor.green)
                             + Math.Abs(color.blue - lastColor.blue);
        }
    }
}
