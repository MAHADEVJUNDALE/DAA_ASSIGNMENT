import java.util.Arrays;

public class fractional {
 
     public class item
     {
        int weight;

        int profit;

        double ratio;

        item( int weight,int profit)
        {

         this.weight = weight;
         this.profit = profit;
         this.ratio = (double) profit / weight ;
        }
     }

    public static void sortitem(item [] it)
    {
        Arrays.sort(it ,(a,b) ->Double.compare(b.ratio, a.ratio));
    }

    public static void fractionalknapsak(item[] it ,int w)
    {
        sortitem(it);

        double total_profit = 0;

        System.out.println("Selected Items");

        for(item itm : it)
        {
            if(w == 0)
            {
                break;
            }

            if(itm.weight <= w)
            {
                w = w - itm.weight;

                total_profit += itm.profit;

                System.out.println("Weight = " + itm.weight + 
                " Profit = " + itm.profit + 
                " Ratio = " + itm.ratio +
                " Taken = 100 %");
            }
            else
            {
                double fraction = (double) w / itm.weight;

                total_profit += (itm.profit * fraction);

                System.out.println("Weight = " + itm.weight + 
                " Profit = " + itm.profit + 
                " Ratio = " + itm.ratio + 
                " Taken = " + (fraction * 100));
            }

        }
        System.out.println(" Maximum Profits " + total_profit);
    }
    public static void main(String [] args)
    {   
        fractional f = new fractional();

        item[] it = {f.new item(10,60),f.new item(20, 100),
            f.new item(30, 120)};

        int w = 50;

        fractionalknapsak(it, w);
    }
}
