import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class merging {

    public static class hospital
    {
        String patient_id;
        String admission_date;
        String admission_time;
        String department;

        hospital(String patient_id,
                String admission_date,
                String admission_time,
                String department)
        {
            this.patient_id = patient_id;
            this.admission_date = admission_date;
            this.admission_time = admission_time;
            this.department = department;
        }

        @Override
        public String toString() {
        return patient_id + " | " +
           admission_date + " | " +
           admission_time + " | " +
           department;
        }
    }
public static void conquer(hospital[] arr ,int low ,int mid,int high)
{
    hospital[] merge = new hospital[high - low + 1];

    int i = low;
    int j = mid + 1;
    int idx = 0;


    while (i <= mid && j <= high) {

        if(arr[i].admission_time.compareTo(arr[j].admission_time) <= 0) {

            merge[idx] = arr[i];
            idx ++;
            i++;
            
        }
        else
        {
            merge[idx] = arr[j];
            idx ++;
            j++;
        }
        
    }

    while (i <= mid) {

        merge[idx] = arr[i];
            idx ++;
            i++; 
    }
    while (j <= high) {

        merge[idx] = arr[j];
            idx ++;
            j++; 
    }


    for(int k = 0;k < merge.length;k++)
    {
        arr[low + k] = merge[k];
    }
}
public static void divide(hospital[] arr , int low,int high)
{
    if(low >= high)
    {
        return ;
    }

    int mid = low + (high - low ) / 2;

    divide(arr, low, mid);
    divide(arr, mid + 1, high);
    conquer(arr,low,mid,high);
}
    public static void main(String[] args)
    {
        String filename = "hospital_patient_records_1_million.csv";

        ArrayList<hospital> list = new ArrayList<>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line;

            br.readLine();


            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

        String patient_id = data[0];
        String admission_date = data[1];
        String admission_time = data[2];
        String department = data[3];

        list.add(new hospital(patient_id, admission_date, admission_time, department));


            }

            br.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        hospital[] hos = list.toArray(new hospital[0]);

        divide(hos, 0, hos.length - 1);

        for(int i = 0; i < hos.length; i++)
        {
            System.out.println(hos[i]);
        }

    }
    
}
