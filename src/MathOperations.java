public class MathOperations implements IArithmeticsAdd, IArithmeticsDiv, IArithmeticsDiff {

	public boolean difference(double a, double b)
	{
		if(a != b)
		{
			return true;
		}
		else
		{
			return false;
		}
   }
   
	public double addition(double a, double b)
	{
		return a+b;
  }
  
	public double division(double a, double b)
	{
		return (double)(a/b);
	}

}
// wszystko wydaje sie byc OK