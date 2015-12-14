package visualization.modeling.uas;

/**
 *
 * @author Robert Lee
 */
public class UASPerformance
{
	//the maximum possible values of the uasBag statistics
	private double stdDevX;
	private double stdDevY;
	private double stdDevZ;
	
	private double maxGS;
	private double minGS;
	private double maxVS;
	private double minVS;
	
	
	public UASPerformance(double stdDevX, double stdDevY,double stdDevZ,double maxGS, double minGS, double maxVS, double minVS)
	{
		this.setStdDevX(stdDevX);
		this.setStdDevY(stdDevY);
		this.setStdDevZ(stdDevZ);
		this.maxGS = maxGS;
		this.minGS = minGS;
		this.maxVS = maxVS;
		this.minVS = minVS;
		
	}	
	
	
	public double getStdDevX() {
		return stdDevX;
	}


	public void setStdDevX(double stdDevX) {
		this.stdDevX = stdDevX;
	}


	public double getStdDevY() {
		return stdDevY;
	}


	public void setStdDevY(double stdDevY) {
		this.stdDevY = stdDevY;
	}


	public double getStdDevZ() {
		return stdDevZ;
	}


	public void setStdDevZ(double stdDevZ) {
		this.stdDevZ = stdDevZ;
	}


	public double getMaxGS() {
		return maxGS;
	}


	public void setMaxGS(double maxGS) {
		this.maxGS = maxGS;
	}


	public double getMinGS() {
		return minGS;
	}


	public void setMinGS(double minGS) {
		this.minGS = minGS;
	}


	public double getMaxVS() {
		return maxVS;
	}


	public void setMaxVS(double maxVS) {
		this.maxVS = maxVS;
	}


	public double getMinVS() {
		return minVS;
	}


	public void setMinVS(double minVS) {
		this.minVS = minVS;
	}

}
