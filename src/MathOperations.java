public class MathOperations implements IArithmeticsAdd, IArithmeticsDiv, IArithmeticsDiff, IArithmeticsMulti 
{
	public double multiplication(double a, double b)
	{
		return a*b;
	}

	//komentarz dawid
	public boolean diffrence (double a, double b)
	{
		if (a != b)
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
// Zad_6 Daniel
//komentarz seba zad 6

