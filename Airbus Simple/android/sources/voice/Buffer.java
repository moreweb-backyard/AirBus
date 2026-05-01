package voice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
public class Buffer {
    private static final String TAG = "Buffer";
    private int mBufferCount;
    private int mBufferSize;
    private BlockingQueue<BufferData> mConsumeQueue;
    private BlockingQueue<BufferData> mProducerQueue;

    public static class BufferData {
        private static BufferData sEmptyBuffer = new BufferData(0);
        public float[] floatData;
        public byte[] mData;
        private int mFilledSize;
        private int mMaxBufferSize;
        private int floatBufferSize = 0;
        private int floatOverlapSize = 0;

        public BufferData(int i) {
            this.mMaxBufferSize = i;
            reset();
            if (i > 0) {
                this.mMaxBufferSize = i;
                this.mData = new byte[this.mMaxBufferSize];
            } else {
                this.mData = null;
            }
        }

        public static BufferData getEmptyBuffer() {
            return sEmptyBuffer;
        }

        public final void reset() {
            this.mFilledSize = 0;
        }

        public final int getMaxBufferSize() {
            return this.mMaxBufferSize;
        }

        public final void setFilledSize(int i) {
            this.mFilledSize = i;
        }

        public final int getFilledSize() {
            return this.mFilledSize;
        }

        public int getFloatBufferSize() {
            return this.floatBufferSize;
        }

        public void setFloatBufferSize(int i) {
            this.floatBufferSize = i;
        }

        public int getFloatOverlapSize() {
            return this.floatOverlapSize;
        }

        public void setFloatOverlapSize(int i) {
            this.floatOverlapSize = i;
        }
    }

    public Buffer() {
        this(3, 1024);
    }

    public Buffer(int i, int i2) throws InterruptedException {
        this.mBufferSize = i2;
        this.mBufferCount = i;
        this.mProducerQueue = new LinkedBlockingQueue(this.mBufferCount);
        this.mConsumeQueue = new LinkedBlockingQueue(this.mBufferCount + 1);
        for (int i3 = 0; i3 < this.mBufferCount; i3++) {
            try {
                this.mProducerQueue.put(new BufferData(this.mBufferSize));
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogHelper.e(TAG, "put buffer data error");
                return;
            }
        }
    }

    public void reset() {
        int size = this.mProducerQueue.size();
        for (int i = 0; i < size; i++) {
            BufferData bufferDataPeek = this.mProducerQueue.peek();
            if (bufferDataPeek == null || bufferDataPeek.mData == null) {
                this.mProducerQueue.poll();
            }
        }
        int size2 = this.mConsumeQueue.size();
        for (int i2 = 0; i2 < size2; i2++) {
            BufferData bufferDataPoll = this.mConsumeQueue.poll();
            if (bufferDataPoll != null && bufferDataPoll.mData != null) {
                this.mProducerQueue.add(bufferDataPoll);
            }
        }
        LogHelper.d(TAG, "reset ProducerQueue Size:" + this.mProducerQueue.size() + "    ConsumeQueue Size:" + this.mConsumeQueue.size());
    }

    public final int getEmptyCount() {
        return this.mProducerQueue.size();
    }

    public final int getFullCount() {
        return this.mConsumeQueue.size();
    }

    public BufferData getEmpty() {
        return getImpl(this.mProducerQueue);
    }

    public boolean putEmpty(BufferData bufferData) {
        return putImpl(bufferData, this.mProducerQueue);
    }

    public BufferData getFull() {
        return getImpl(this.mConsumeQueue);
    }

    public boolean putFull(BufferData bufferData) {
        return putImpl(bufferData, this.mConsumeQueue);
    }

    private BufferData getImpl(BlockingQueue<BufferData> blockingQueue) {
        if (blockingQueue == null) {
            return null;
        }
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean putImpl(BufferData bufferData, BlockingQueue<BufferData> blockingQueue) throws InterruptedException {
        if (blockingQueue == null || bufferData == null) {
            return false;
        }
        try {
            blockingQueue.put(bufferData);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
