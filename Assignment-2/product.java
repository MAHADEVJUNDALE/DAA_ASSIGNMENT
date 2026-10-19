import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class product {

    public class products
    {
        String product_id;
        String product_name;
        double price;
        double rating;
        int sales_volume;

        products( String product_id,
        String product_name,
        double price,
        double rating,
        int sales_volume)
        {
        this.product_id = product_id;
        this.product_name = product_name;
        this.price = price;
        this.rating = rating;
        this.sales_volume = sales_volume;

        }

        @Override 

        public String toString()
        {
            return product_id + " | " +
            product_name + " | " +
            price + " | " +
            rating + " | " +
            sales_volume + " | " ;
        }
    }
    public static int partition(products[] arr,int low, int high)
    {
        products pivot = arr[low];

        int i = low + 1;

        int j = high;

        while(i <= j)
        {
            while(i <= high && arr[i].price <= pivot.price)
            {
                i++;
            }

             while(j > low && arr[j].price >= pivot.price)
            {
                j--;
            }

            if(i < j)
            {
                products temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }

        }
                products temp = arr[low];
                arr[low] = arr[j];
                arr[j] = temp;

                return j;
    }
    public static void quicksort(products[] arr , int low , int high)
    {
        if(low >= high)
        {
            return ;
        }
        
        int pivot = partition(arr , low , high);

        quicksort(arr, low, pivot - 1);
        quicksort(arr, pivot + 1, high);
    }
    public static void main(String[] args)
    {
        String fileName = "products.csv";

        ArrayList<products> list = new ArrayList<>();

        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                String product_id = data[0];
                String product_name = data[1];
                double price = Double.parseDouble(data[2]);
                double rating = Double.parseDouble(data[3]);
                int sales_volume = Integer.parseInt(data[4]);

                list.add(new product().new products(
                        product_id,
                        product_name,
                        price,
                        rating,
                        sales_volume
                ));

                
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        products[] pro = list.toArray(new products[0]);

        quicksort(pro, 0, pro.length - 1);
          for (int i = 0; i < Math.min(10, pro.length); i++) {
            System.out.println(pro[i]);
        }
    }
}
