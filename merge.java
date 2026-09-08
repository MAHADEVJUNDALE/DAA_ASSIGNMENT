import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class merge
{    
    static class hospital
    {
    
    String PatientID;
    String Name;
    String DatofBirth;
    String Gender;
    String MedicalCondition;
    String Treatments;
    String DoctorsNotes;
    String AdmitDate;
    String DischargeDate;
    String BillAmount;

    hospital( String PatientID,
    String Name,
    String DatofBirth,
    String Gender,
    String MedicalCondition,
    String Treatments,
    String DoctorsNotes,
    String AdmitDate,
    String DischargeDate,
    String BillAmount)
    {
        this.PatientID = PatientID;
        this.Name = Name;
        this.DatofBirth = DatofBirth;
        this.Gender = Gender;
        this. MedicalCondition = MedicalCondition;
        this. Treatments = Treatments;
        this. DoctorsNotes = DoctorsNotes;
        this.AdmitDate = AdmitDate;
        this. DischargeDate = DischargeDate;
        this. BillAmount = BillAmount;
    }

    }
     public static LocalDate getDate(String date) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return LocalDate.parse(date, formatter);
    }
    public static void conquer(hospital[] arr,int s,int mid , int e)
    {
        hospital[] temp = new hospital[e - s + 1];
        int i = s;
        int j = mid + 1;
        int idx = 0;

            while(i <= mid && j <= e)
            {
               if(getDate(arr[i].AdmitDate)
        .compareTo(getDate(arr[j].AdmitDate)) <= 0)
                 {
                    temp[idx] = arr[i];
                    i++;
                    idx++;

                 }
                 else
                 {
                    temp[idx] = arr[j];
                    j++;
                    idx++;
                 }
            }
             while(i <= mid)
            {
    
                    temp[idx] = arr[i];
                    i++;
                    idx++;

            }
             while(j <= e)
            {
    
                    temp[idx] = arr[j];
                    j++;
                    idx++;

            }
        

        for(i = 0;i < temp.length;i++)
        {
            arr[s + i] = temp[i];
        }
    }   
    public static void divide(hospital[] arr,int s , int e)
    {  
        if(s < e)
        {
            
        int mid = s + (e - s) / 2;

        divide(arr , s , mid);
        divide(arr , mid + 1 , e);
        conquer(arr , s , mid ,e);

        }
        

    }

    public static ArrayList<hospital> readCSV(String fileName)
    {
        ArrayList<hospital> hos = new ArrayList<>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line = br.readLine();

            while ((line = br.readLine()) != null)
         {
                String[] data = line.split(",");

                if(data.length >= 10)
                {
                    hospital h = new hospital(data[0].trim(),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim(),
                        data[4].trim(),
                        data[5].trim(),
                        data[6].trim(),
                        data[7].trim(),
                        data[8].trim(),
                        data[9].trim()
                    );

                    hos.add(h);
          }
         }
          br.close();
       
        }
         
        catch(Exception e)
        {
            System.out.print("File Not Read ");

            e.printStackTrace();
        }

        return hos;
    }
    public static void main(String args[])
    {
        String fileName = "hospital_records_2021_2024_with_bills.csv";
        
        ArrayList<hospital> list = readCSV(fileName);
        hospital[] arr = list.toArray(new hospital[0]);
        System.out.println("Sorted Array");

        divide(arr, 0, arr.length - 1);

        for(int i = 0; i <Math.min(10 , arr.length);i++)
        {
             System.out.printf("%-12s %-25s %-15s %-15s%n",
            arr[i].PatientID,
            arr[i].Name,
            arr[i].AdmitDate,
            arr[i].BillAmount);

        }
    }
}