using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Accord.Video.FFMPEG;

namespace ObjectTracker
{
    public class VideoFrameReader : IDisposable
    {
        public int Width => _underlyingReader.Width;
        public int Height => _underlyingReader.Height;
        private readonly VideoFileReader _underlyingReader;

        public VideoFrameReader(string filePath)
        {
            _underlyingReader = new VideoFileReader();
            _underlyingReader.Open(filePath);
        }

        /// <summary>
        /// Processes all frames of a video
        /// </summary>
        /// <param name="onFrameProcessed">Provides a bitmap and an index of a frame that is processing</param>
        /// <returns>The total number of frames</returns>
        public int ReadAllFrames(Action<Bitmap, int> onFrameProcessed)
        {
            Bitmap frameBitmap;
            int index = 0;
            while ((frameBitmap = _underlyingReader.ReadVideoFrame()) != null)
            {
                onFrameProcessed.Invoke(frameBitmap, index);
                index++;
            }

            var frameCount = index;
            return frameCount;
        }

        public void Dispose()
        {
            _underlyingReader.Dispose();
        }
    }
}
