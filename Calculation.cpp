#include <iostream>


  class Program { //comment
	
};

/*
 * This is a class
 *
 *
 *
 */
class Calculation :Program
{
  private:
    int var1;  // This is var1 */ /*
    int var2;  /* This is var 2 */ //This is a comment
    int result;/* This is var 2 //This is a comment */
 // std::string var = "I hope this works";
    std::string var = "I hope this works";
    
        std::string vara = "I hope this works"+"You should not be here" + "Yes, you!";
        
        std::string varb = " This \
        is a multi-line \
        comment ";

  public:
  // This is a comment
    int getSum()
    {
      return var1 + var2;
    }
    /* Even me
    
    */
    int getDiff()
    {
      return var1 - var2;
    }
    /*This */ /* is // */ /*the */ //crazziest!
    void setNums(int a, int b)
    {
      var1 = a;
      var2 = b;
    }
};class Dummy:Program{
};

int main()
{
  Calculation obj;
  obj.setNums(4, 5);
  std::cout << obj.getSum() << std::endl;
  std::cout << obj.getDiff() << std::endl;
  return 1;
}
