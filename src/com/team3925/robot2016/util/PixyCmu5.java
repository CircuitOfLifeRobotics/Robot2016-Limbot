package com.team3925.robot2016.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class implements the interface to the Pixy Camera via I2C.
 *
 * Creates and manages the interface to the Pixy Camera. Able to create a separate thread to process frames 
 * periodically and return the frame data for any detected objects.
 */
public class PixyCmu5 implements PIDSource
{
	
	/**
     * The frame data that is returned by the Pixy camera over the I2C interface
     */
    public class PixyFrame
    {
        int sync = 0;
        int checksum = 0;
        public double timestamp = 0;
        public int signature;
        public int xCenter;
        public int yCenter;
        public int width;
        public int height;
        public int angle;
        public int area;
    }
    
	/**********************************************************
	 *  Pixy CMU Communication Parameters
	 **********************************************************/
	public static final int PIXY_ARRAYSIZE               = 130;
	public static final int PIXY_START_WORD              = 0xaa55;
	public static final int PIXY_START_WORD_CC           = 0xaa56;
	public static final int PIXY_START_WORDX             = 0x55aa;
	public static final int PIXY_SERVO_SYNC              = 0xff;
	public static final int PIXY_CAM_BRIGHTNESS_SYNC     = 0xfe;
	public static final int PIXY_LED_SYNC                = 0xfd;
	public static final int PIXY_OUTBUF_SIZE             = 64;
	public static final int PIXY_SYNC_BYTE               = 0x5a;
	public static final int PIXY_SYNC_BYTE_DATA          = 0x5b;
	
	/**********************************************************
	 * Define Constants for the image size
	 *********************************************************/
	public static final int PIXY_MIN_X = 0;
	public static final int PIXY_MAX_X = 319;
	public static final double PIXY_X_CENTER = (PIXY_MIN_X + PIXY_MAX_X)/2;
	public static final int PIXY_MIN_Y = 0;
	public static final int PIXY_MAX_Y = 199;
	public static final double PIXY_Y_CENTER = (PIXY_MIN_Y + PIXY_MAX_Y)/2;
	
	/**********************************************************
	 * Stock field of view is 75 degrees horizontal and 47 degrees vertical
	 *********************************************************/
	public static final double PIXY_X_FOV = 75.0;
	public static final double PIXY_Y_FOV = 47.0;
	public static final double PIXY_X_DEG_PER_PIXEL = PIXY_X_FOV/(PIXY_MAX_X+1);
	public static final double PIXY_Y_DEG_PER_PIXEL = PIXY_Y_FOV/(PIXY_MAX_Y+1);

	
	/**********************************************************
	 *  Pixy CMU I2C Interface
	 **********************************************************/
    private static final int DATA_SIZE = 130;
    
    /**********************************************************
	 *  Local Variables
	 **********************************************************/
    private List<PixyFrame> m_currentframes;
    private double m_lastupdate = 0.0;
    private byte m_zeroBuffer[];
    private double m_maxDataAge = 1.5; // second
    private boolean m_isreading = false;

	private double m_centerDelta = 10.0; // Pixels
    
	/**********************************************************
	 *  Threading
	 **********************************************************/
    java.util.Timer m_scheduler;    // The timer object to manage the thread
    private double m_period = 0.1; // 1/4 of a second seems to be a stable update rate
    
    /**********************************************************
	 *  PID Interface Variables
	 **********************************************************/
    PIDSourceType m_pid_source_type = PIDSourceType.kDisplacement;
    
    /**********************************************************
	 *  I2C
	 **********************************************************/
    public static final int PIXY_I2C_DEFAULT_ADDR = 0x54; //0xa8;
    private I2C m_i2cbus;
    private int m_i2caddress = PIXY_I2C_DEFAULT_ADDR;
    
    /**********************************************************
	 *  Debug variables
	 **********************************************************/
    private static boolean flg_debug = false;
    
	/**
	 * PixyTask is the private scheduler within PixyCMU5 that 
	 * automatically performs I2C reads to get the frame data
	 * from the connected Pixy device.
	 */
	private class PixyTask extends TimerTask {

        private PixyCmu5 m_pixy;

        public PixyTask(PixyCmu5 pixy) {
          if (pixy == null) {
            throw new NullPointerException("Pixy Instance was null");
          }
          this.m_pixy = pixy;
        }

        @Override
        public void run() {
        	m_pixy.getFrames();
        }
      }
    
	/**************************************************************
	 * 
     * CONSTRUCTORS--CONSTRUCTORS--CONSTRUCTORS--CONSTRUCTORS
     * 
     **************************************************************/
	
	/**
     * The constructor for the PixyCMU5 class. Initializes the I2C bus at the default address
     * $
     */
    public PixyCmu5()
    {
    	m_i2cbus = new I2C(I2C.Port.kOnboard, getI2CAddress());
    	m_currentframes = new LinkedList<PixyFrame>();
    	m_zeroBuffer = new byte [DATA_SIZE];
    	Arrays.fill(m_zeroBuffer, (byte)0);    	
    }
    
	/**
     * The constructors for the PixyCMU5 class. Initializes the I2C bus at the specified address
     * $
     * @param i2c_address_in - The I2C address to connect to
     * @param period - The period in seconds to perform I2C reads
     */
    public PixyCmu5(int i2c_address_in)
    {
    	setI2CAddress(i2c_address_in);
    	m_i2cbus = new I2C(I2C.Port.kOnboard, getI2CAddress());
    	m_currentframes = new LinkedList<PixyFrame>();
    	m_zeroBuffer = new byte [DATA_SIZE];
    	Arrays.fill(m_zeroBuffer, (byte)0);
    }
    
    /**
     * The constructors for the class. Initialize any data and the bus
     * $
     * @param i2c_address_in - The I2C address to connect to
     * @param period - The period in seconds to perform I2C reads
     */
    public PixyCmu5(int i2c_address_in, double period)
    {
    	// Set I2C address and period
    	this.setI2CAddress(i2c_address_in);
    	this.setPeriod(period);
    	m_i2cbus = new I2C(I2C.Port.kOnboard, getI2CAddress());
    	m_currentframes = new LinkedList<PixyFrame>();
    	m_zeroBuffer = new byte [DATA_SIZE];
    	Arrays.fill(m_zeroBuffer, (byte)0);
    	
    	// Call the start method to schedule reading the data periodically
    	this.start(getPeriod());
    }
    
    
    /**
     * Start a thread to read data from the Pixy periodically
     * 
     * @param period - period in seconds to schedule update
     */
    public void start(double period)
    {
   	
    	// Schedule the Pixy task to execute every <period> seconds
    	if(m_scheduler == null && period > 0)
    	{
    		this.setPeriod(period);
    		System.out.println("Attempting to enable Pixy at a " + Double.toString(period) + " second rate.");
    		m_scheduler = new java.util.Timer();
    		m_scheduler.schedule(new PixyTask(this), 0L, (long) (this.getPeriod() * 1000));
    	} else {
    		System.out.println("Pixy Thread already scheduled. Stop before starting a new thread.");
    	}
    }
    
    
    /**
     * Cancel a running timer and attempt to null the variable
     */
    public void stop()
    {
    	// If the timer object is not null, cancel the scheduler
    	if(m_scheduler != null)
    	{
    		System.out.println("Attempting to disable Pixy auto polling.");
    		m_scheduler.cancel();
    		
    		// Set the timer to NULL to allow it to be reallocated if necessary
    		synchronized(this) {
    			m_scheduler = null;
    		}
    		
    	} else {
    		// nothing to do
    	}
    }
    
    
    /**
     * Performs an I2C read at the specified address and decodes any data received.
     * $
     * @return linked list of PixyFrame objects for any detected objects
     */
    public List<PixyFrame> getFrames()
    {
    	// Initialize the local linked list for the results
    	List<PixyFrame> frames = new LinkedList<PixyFrame>();
    	
    	// Read lock code
    	if(this.getIsReading())
    	{
    		return frames;
    	}
    	this.setIsReading(true);
    	
    	// readBuffer is the raw data output from the i2c bus
    	byte readBuffer[] = new byte [DATA_SIZE];

    	// Read data from the i2c bus 
    	m_i2cbus.readOnly(readBuffer, DATA_SIZE);

    	if(Arrays.equals(m_zeroBuffer, readBuffer))
    	{
    		if(flg_debug)
    		{
    			System.out.println("Pixy - Read Failed! All elements returned were 0!");
    		}

    		// Add synchronization block to assign frame output to class
        	synchronized (this)
        	{
        		if(m_currentframes != null)
        		{	
        			m_lastupdate = Timer.getFPGATimestamp();
    	    		m_currentframes.clear();
        		}
        	}
        	this.setIsReading(false);
    		return frames;
    	}
    	
    	if(flg_debug)
    	{
    		// Allocate debug string
    		String readBuffer_char = "";
        	
    		// Build the string for debug printouts        	
    		for(int idx = 0; idx < readBuffer.length; idx++)
    		{
    			readBuffer_char += Integer.toString(readBuffer[idx] & 0xFF) + " ";
    		}
    		// Print the raw buffer to the console
    		System.out.println(readBuffer_char);
    	}
    	
    	/* 
    	 * Move through the array and look for the pattern that indicates the start of a frame.
    	 * Frame data is encoded as follows:
    	 * 
         * Bytes    16-bit word    Description
	     * ----------------------------------------------------------------
	     * 0, 1     y              sync: 0xaa55=normal object, 0xaa56=color code object
	     * 2, 3     y              checksum (sum of all 16-bit words 2-6)
	     * 4, 5     y              signature number
	     * 6, 7     y              x center of object
	     * 8, 9     y              y center of object
	     * 10, 11   y              width of object
	     * 12, 13   y              height of object
    	 */
    	for(int idx = 0; idx < readBuffer.length - 18; idx++ ) 
    	{
    		/*
    		 * The Pixy sends the data in Little Endian format [https://en.wikipedia.org/wiki/Endianness]
	    	 *  with the data packed in 16 bit integers (4 hex digits each) [0xFF]. Little Endian format 
	    	 *  means that the bytes (one hex digit) are sent with the least-significant byte first, so they
	    	 *  need to be reversed before combining them into a 16 bit word.
    		 * */
    		
    		// Two Pixy start words 0xAA55 indicate that 
    		if( !( (((readBuffer[idx+1] << 8) | readBuffer[idx] ) & 0xFFFF) == PIXY_START_WORD  ))
    		{
    			// If this word wasn't found, cancel executing the rest of the loop iteration and move to the next byte
    			continue; 
    		}
    		
    		if( !( (((readBuffer[idx+3] << 8) | readBuffer[idx+2] ) & 0xFFFF)  == PIXY_START_WORD ))
    		{
    			// If this word wasn't found, cancel executing the rest of the loop iteration and move to the next byte
    			continue;
    		}
    		
    		/* If we make it this far, we found two instances of 0xaa55 back-to-back which means that the next 14 bytes
    		 * make up the data for the frame. Create a new instance of the Frame class (which acts as a structure) and start 
    		 * pulling out byte pairs and switching them before packing them into integers. Since some of the data 
    		 * on the i2c bus is encoded as unsigned 16 bit integers and Java doesn't have unsigned types, we need to manually
    		 * re-encode the data into 32 bit integers which can correctly represent the value.
    		 * [https://en.wikipedia.org/wiki/Integer_(computer_science)]
    		 * 
    		 * To do this we treat the 16 bit unsigned value as a 32 bit signed value (how Java stores integers)
    		 * so the real-world value that we can work with represents the correct data.
    		 */
        	PixyFrame tempFrame = new PixyFrame();
    		
    		int startIdx = idx+4;
    		tempFrame.checksum = convertBytesToInt(readBuffer[startIdx+1], readBuffer[startIdx]); startIdx+=2;
    		tempFrame.signature = convertBytesToInt(readBuffer[startIdx+1], readBuffer[startIdx]); startIdx+=2;
    		tempFrame.xCenter = convertBytesToInt(readBuffer[startIdx+1], readBuffer[startIdx]); startIdx+=2;
    		tempFrame.yCenter = convertBytesToInt(readBuffer[startIdx+1], readBuffer[startIdx]); startIdx+=2;
    		tempFrame.width = convertBytesToInt(readBuffer[startIdx+1], readBuffer[startIdx]); startIdx+=2;
    		tempFrame.height = convertBytesToInt(readBuffer[startIdx+1], readBuffer[startIdx]); startIdx+=2;
    		tempFrame.area = tempFrame.height * tempFrame.width;
    		tempFrame.timestamp = m_lastupdate;
    		idx = startIdx;
    		
    		// Concatenate the data in Frame into a string and print to the console
    		if(flg_debug)
    		{
	    		System.out.println("Checksum: "+ Integer.toString(tempFrame.checksum) + 
	    				" Signature: "+ Integer.toString(tempFrame.signature) +
	    				" xCenter: "+ Integer.toString(tempFrame.xCenter) + 
	    				" yCenter: "+ Integer.toString(tempFrame.yCenter) +
	    				" width: "+ Integer.toString(tempFrame.width) +
	    				" height: "+ Integer.toString(tempFrame.height));
	    		
	    		SmartDashboard.putNumber("Checksum", tempFrame.checksum); 
				SmartDashboard.putNumber("Signature", tempFrame.signature);
				SmartDashboard.putNumber("XCenter", tempFrame.xCenter);
				SmartDashboard.putNumber("YCenter", tempFrame.yCenter);
				SmartDashboard.putNumber("Width", tempFrame.width);
				SmartDashboard.putNumber("Height", tempFrame.height);
    		}
    		
    		// Append the constructed frame to the Linked List which will be returned to the caller
    		frames.add(tempFrame);
    	}
    	
    	// Add synchronization block to assign frame output to class
    	synchronized (this)
    	{
    		if(m_currentframes != null)
    		{	
    			m_lastupdate = Timer.getFPGATimestamp();
	    		m_currentframes.clear();
	    		m_currentframes = frames;
    		}
    	}

    	this.setIsReading(false);
    	return frames;
    }
    
    
    /**************************************************************
     * 
     * HELPER METHODS--HELPER METHODS--HELPER METHODS
     * 
     **************************************************************/
    
    
    
    /**
     * Returns true if a signature is detected and data is still fresh
     * 
     * @return true if object is detected, false if object is not detected
     */
    public boolean isObjectDetected()
    {
    	// If the data is fresh and objects have been detected
    	if(getDataAge() < m_maxDataAge && !getCurrentframes().isEmpty())
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Calculates if the object is in the center of the frame with
     * a margin of error specified
     * $
     * @param X - X location in pixels
     * @return true if centered
     */
    public boolean isInXCenter(double X)
    {
    	// See if the X center is in the middle of the frame
    	if(Math.abs(X - PIXY_X_CENTER) < m_centerDelta)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Calculates if the object is in the center of the frame with
     * a margin of error specified
     * $
     * @param X - X location in pixels
     * @return true if centered
     */
    public boolean isInXCenter(PixyFrame frame)
    {
    	// See if the X center is in the middle of the frame
    	if(Math.abs(frame.xCenter - PIXY_X_CENTER) < m_centerDelta)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Calculates if the object is in the center of the frame with
     * a margin of error specified
     * $
     * @param X - X location in pixels
     * @return true if centered
     */
    public boolean isInYCenter(double Y)
    {
    	// See if the Y center is in the middle of the frame
    	if(Math.abs(Y - PIXY_Y_CENTER) < m_centerDelta)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Calculates if the object is in the center of the frame with
     * a margin of error specified
     * $
     * @param X - X location in pixels
     * @return true if centered
     */
    public boolean isInYCenter(PixyFrame frame)
    {
    	// See if the Y center is in the middle of the frame
    	if(Math.abs(frame.yCenter - PIXY_Y_CENTER) < m_centerDelta)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    
    /**
     * Checks to see if the most recent data is still valid or if the time has expired
     * $
     * @return true if valid, false if invalid
     */
    public boolean isDataFresh()
    {
    	return this.getDataAge() <= this.getMaxDataAge();
    }
    
    /**
     * Returns true if a signature is detected and ANY of them are in the center of the frame
     * 
     * @return
     */
    public boolean isDetectedAndCentered()
    {
    	// If the data is fresh and objects have been detected
    	if(getDataAge() < m_maxDataAge && !getCurrentframes().isEmpty())
    	{
    		for(PixyFrame idx : getCurrentframes())
    		{
    			if(isInXCenter(idx.xCenter))
    			{
    				return true;
    			}
    		}	
    	}
    	return false;
    }
    
    /**
     * Sets the brightness value of the Pixy camera
     * $
     * @param brightness - 0-255
     * @return false if success, true if abort
     */
    public boolean setBrightness(byte brightness)
    {
    	byte[] outBuf = new byte[3];
    	
    	outBuf[0] = 0x00;
    	outBuf[1] = (byte)PIXY_CAM_BRIGHTNESS_SYNC;
    	outBuf[2] = brightness;
    	
    	// Perform the I2C Transaction
    	return m_i2cbus.transaction(outBuf, 3, null, 0);
    }
    
    /**
     * Sets the RGB LED value of the Pixy camera
     * $
     * @param R
     * @param G
     * @param B
     * @return false if success, true if abort
     */
    public boolean setLED(byte R, byte G, byte B)
    {
    	byte[] outBuf = new byte[5];
    	
    	outBuf[0] = 0x00;
    	outBuf[1] = (byte)PIXY_LED_SYNC;
    	outBuf[2] = R;
    	outBuf[3] = G;
    	outBuf[4] = B;
    	
    	// Perform the I2C Transaction
    	return m_i2cbus.transaction(outBuf, 5, null, 0);
    }
    
    /**************************************************************
     * 
     * STATICS--STATICS--STATICS--STATICS--STATICS--STATICS
     * 
     **************************************************************/
    
    /**
     * Converts two unsigned bytes to a signed integer
     * $
     * @param msb - Most significant byte
     * @param lsb - Least significant byte
     * @return - Integer value
     */
    public static int convertBytesToInt(int msb, int lsb)
    {
        if (msb < 0)
            msb += 256;
        int value = msb * 256;

        if (lsb < 0)
        {
            // lsb should be unsigned
            value += 256;
        }
        value += lsb;
        return value;
    }
    
    
    /**
     * Returns the offset from the X center, negative indicates center is to the left, positive indicates it is to the right.
     * $
     * @param frame PixyFrame representing the detected object
     * @return number of pixels from center
     */
    public static double xCenterDelta(PixyFrame frame)
    {
    	return frame.xCenter - PixyCmu5.PIXY_X_CENTER;
    }
    
    /**
     * Returns the number of degrees along the horizontal axis the detected object is away from the center
     * $
     * @param frame PixyFrame representing the detected object
     * @return degrees along x axis from center of field of view
     */
    public static double degreesXFromCenter(PixyFrame frame)
    {
    	return xCenterDelta(frame)*PIXY_X_DEG_PER_PIXEL;
    }
    
    /**************************************************************
     * 
     * GETTERS/SETTERS--GETTERS/SETTERS--GETTERS/SETTERS
     * 
     **************************************************************/
    
    /**
     * @return currentFrames - Returns the thread synchronized list of current frames
     */
    public synchronized List<PixyFrame> getCurrentframes() {
		return m_currentframes;
	}
    
    /**
     * @return m_lastupdate - The FPGA timestamp of the last time the frame was processed.
     */
    public synchronized double getLastupdate()
    {
    	return m_lastupdate;
    }
    
    /**
     * The number of seconds since the last Pixy Frame was processed.
     * $
     * @return dataAge - The number of seconds between the current timestamp and the last time the Pixy Frame was processed
     */
    public synchronized double getDataAge()
    {
    	return Timer.getFPGATimestamp() - this.getLastupdate();
    }
    
    /**
     * Sets the frequency the read process will occur at.
     * $
     * @param period in seconds
     */
    public synchronized void setPeriod(double period)
    {
    	if(period < 0)
    	{
    		throw new IllegalArgumentException("Period must be a positive value");
    	}
    	
    	m_period = period;
    }
    
    /**
     * Gets the frequency the read process will occur at.
     * $
     * @return period in seconds
     */
    public synchronized double getPeriod()
    {
    	return this.m_period;
    }
    
    /**
     * Sets the i2c address of the Pixy to connect to
     * $
     * @param i2c_address
     */
    public synchronized void setI2CAddress(int i2c_address)
    {
    	if(i2c_address <= 0 || i2c_address > 255)
    	{
    		throw new IllegalArgumentException("Invalid I2C address for Pixy Camera");
    	}
    	
    	this.m_i2caddress = i2c_address;
    }
    
    /**
     * Gets the i2c address of the Pixy we are trying to communicate with
     * $
     * @return i2c address
     */
    public synchronized int getI2CAddress()
    {
    	return this.m_i2caddress;
    }
    
    
    /**
	 * Gets the maximum data age for a frame to consider it still valid
	 * $
	 * @return seconds a frame should be considered valid after it's been read
	 */
    public double getMaxDataAge() {
		return m_maxDataAge;
	}

    /**
	 * Sets the maximum data age for a frame to consider it still valid
	 * $
	 * @double maxDataAge - seconds a frame should be considered valid after it's been read
	 */
	public void setMaxDataAge(double maxDataAge) {
		this.m_maxDataAge = maxDataAge;
	}

	/**
	 * Gets the number of pixels an object can be away from the center for it to be considered centered
	 * $
	 * @return centerDelta
	 */
	public double getCenterDelta() {
		return m_centerDelta;
	}
	
	/**
	 * Sets the number of pixels an object can be away from the center for it to be considered centered
	 * $
	 * @param centerDelta
	 */
	public void setCenterDelta(double centerDelta) {
		this.m_centerDelta = centerDelta;
	}
	
	/**
	 * Sets the internal read flag to indicate an I2C transaction is still in process
	 * $
	 * @param centerDelta
	 */
	private synchronized void setIsReading(boolean reading)
	{
		this.m_isreading = reading;
	}
	
	/**
	 * Gets the internal read flag to indicate an I2C transaction is still in process
	 * $
	 * @param centerDelta
	 */
	public synchronized boolean getIsReading()
	{
		return this.m_isreading;
	}

	
	/**************************************************************
     * PID SOURCE INTERFACE
     **************************************************************/
    
    public PIDSourceType getPIDSourceType() {
    	return m_pid_source_type;
    }
    
    public void setPIDSourceType(PIDSourceType type) {
    	m_pid_source_type = type;
    }

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.PIDSource#pidGet()
	 * Implement the pidGet method
	 */
	@Override
	public double pidGet() {
		List<PixyFrame> pixyFrames;
		
		// Synchronize with the thread and latch the current frames
		pixyFrames = this.getCurrentframes();
		
		// If there are no frames processed or the data is too old
		if(pixyFrames.isEmpty() || !isDataFresh())
		{
			return 0;
		}
		
		return PixyCmu5.degreesXFromCenter(pixyFrames.get(0));
	}

}