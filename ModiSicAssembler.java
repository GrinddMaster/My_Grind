import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
/* Project Made by
*Ali Hussein
*Kareem Abu El Azm
*Nour Tarek
*/

public class Assemble{
    
    public static void main(String[] args) throws FileNotFoundException {

        //HashMap for the Instruction Set:=======================================================================================================>
        InstructionSet.getI();

        //HashMap for the Symbol Table:==========================================================================================================>
        HashMap<String,Integer> Symbol_Table = new HashMap<>(); 
        
        //ArrayList for object code:=============================================================================================================>
        ArrayList<String> Object_Code = new ArrayList<String>();

        //ArrayList for Masking bits:============================================================================================================>
        ArrayList <String> RelocBits = new ArrayList<>();

        ArrayList<Integer> nodes= new ArrayList<>();

        //ArrayList for T-Record Start Locs:=====================================================================================================>
        ArrayList<String> tStarts = new ArrayList<>();
        boolean flag = false;
        HashMap<String,ArrayList<Integer>>FwdRef = new HashMap<>();
        int LOCCTR;
        int count=0;
        String[]withX=new String[2];
        String[]immediate = new String[2];
        withX[1] = "";
        String objCode="no obj code";
        String Top ;
        String maskBit = "";
        ArrayList<String> format1 = new ArrayList<>();
        format1.add("FIX");
        format1.add("NORM");
        format1.add("HIO");
        format1.add("FLOAT");
        format1.add("SIO");
        format1.add("TIO");
        format1.add("RSUB");
        ArrayList <String> assembly = new ArrayList<>();
        File assem = new File("ASSEM.txt");
        String[] instLine = new String[3];
        String assemLine = "";
        Scanner scanAssem = new Scanner(assem);

        String T = "";
        ArrayList<String> HTE = new ArrayList<>();
        //To get Program name and Starting address:==============================================================================================>
        Top=scanAssem.nextLine();
        String[] topRow=Top.split("\\s+");
        instLine = Top.split("\\s+");

        //Initialize the LOCCTR with the starting address:=======================================================================================>
        LOCCTR = Integer.parseInt(instLine[2],16);
        assembly.add(Integer.toHexString(LOCCTR)+"\t"+Top);
        //Scans the ASSEMBLY.txt file into an arrylist:==========================================================================================>
        while(scanAssem.hasNext()){
            assemLine = scanAssem.nextLine();
            assembly.add(toFour(Integer.toHexString(LOCCTR))+"\t"+assemLine);

            //Splits the line of assembly code by spaces detected:===============================================================================>
            instLine = assemLine.split("\\s+");
            
            if(instLine.length<3){
                instLine=ArrayExtend(instLine);
            }
            //instLine[0] is the symbol, instLine[1] is the instruction/operation, instLine[2] is the operand:===================================>
            if(!instLine[1].equals("END")){
                if(FwdRef.containsKey(instLine[0])){
                    RelocBits.add(toThree(Integer.toHexString(Integer.valueOf(toTwelve(maskBit+00),2))));
                    maskBit="";
                    tStarts.add(toFour(Integer.toHexString(LOCCTR)));
                }
                if(maskBit.length()==10){
                    RelocBits.add(toThree(Integer.toHexString(Integer.valueOf(toTwelve(maskBit+00),2))));
                    maskBit="";
                    tStarts.add(toFour(Integer.toHexString(LOCCTR)));
                }

                if(format1.contains(instLine[1])||instLine[1].equals("WORD")||instLine[1].equals("BYTE")||instLine[1].equals("RSUB")){
                    maskBit+="0";
                    flag=false;
                }
                else if(InstructionSet.containsInst(instLine[1])&&!format1.contains(instLine[1])){
                    if(maskBit.endsWith("0")){
                        RelocBits.add(toThree(Integer.toHexString(Integer.valueOf(toTwelve(maskBit),2))));
                        tStarts.add(toFour(Integer.toHexString(LOCCTR)));
                        maskBit="1";
                    }
                    else{
                        maskBit+=1;
                    }
                    flag=false;
                }
                
                if(instLine[1].equals("RESB")||instLine[1].equals("RESW")){
                    if(flag){
                        if(instLine[1].equals("RESB"))
                            tStarts.add(toFour(Integer.toHexString(LOCCTR+Integer.parseInt(instLine[2]))));
                        else
                            tStarts.add(toFour(Integer.toHexString(LOCCTR+Integer.parseInt(instLine[2])*3)));
                    }
                    else{
                        RelocBits.add(toThree(Integer.toHexString(Integer.valueOf(toTwelve(maskBit),2))));
                    }        
                    maskBit="";
                    flag=true;
                }                   
            }
            else{
                RelocBits.add(toThree(Integer.toHexString(Integer.valueOf(toTwelve(maskBit),2))));
                maskBit="";
            }
            if(!instLine[0].equals(""))
            {
                if(Symbol_Table.containsKey("*"+instLine[0])){                
                    Symbol_Table.remove("*"+instLine[0]);
                    Symbol_Table.put(instLine[0], LOCCTR);
                    System.out.println(instLine[0]+"\t"+Integer.toHexString(LOCCTR)+"\t");
                }
                else{
                    Symbol_Table.put(instLine[0],LOCCTR);
                    System.out.println(instLine[0]+"\t"+Integer.toHexString(LOCCTR)+"\t");
                }   
            }
            //LOCCTR conditions:=================================================================================================================>
            if(instLine[1].equals("RESW"))
            {
                Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                Object_Code.add("");
                LOCCTR+=Integer.parseInt(instLine[2])*3;
            }
            else if(instLine[1].equals("RESB"))
            {
                Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                Object_Code.add("");
                LOCCTR+=Integer.parseInt(instLine[2]);
            }
            else if(format1.contains(instLine[1])||instLine[1].equals("BYTE"))
            {   
                //Gets the opcode for the corresponding Instruction(Format 1):===================================================================>
                if(format1.contains(instLine[1])){
                    objCode=(InstructionSet.getKey(instLine[1])).toUpperCase();
                    if(instLine[1].equals("RSUB")){
                        Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                        Object_Code.add(toSix(objCode));
                        LOCCTR+=2;
                    }
                    else{
                       Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                       Object_Code.add(toTwo(objCode)); 
                    }   
                }
                else{
                    if(instLine[1].equals("BYTE") && instLine[2].startsWith("C'")){
                        String [] bytes=instLine[2].split("'");
                        char[]bytesToChar = bytes[1].toCharArray();
                        String ascii="";
                        int size=bytesToChar.length;
                        for(int l=0;l<bytesToChar.length;l++){
                            ascii+=Integer.toHexString((int)bytesToChar[l]);
                        }
                        Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                        Object_Code.add(ascii);
                        LOCCTR+=size-1;
                    }
                    else{
                        Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                        Object_Code.add(toTwo(ByteHandler(instLine[2]))); // Now I have the byte's value
                    }                         
                } 
                LOCCTR+=1;
            }
            else
            {
                //Gets the opcode for the corresponding Instruction(Format 3):===================================================================>
                if(!instLine[1].equals("WORD")&&!instLine[1].equals("END")&&!instLine[1].equals("RSUB")){               
                    
                    //Handles ",X":==============================================================================================================>
                    if(instLine[2].contains(",X")){
                        withX = instLine[2].split(",");
                        instLine[2]=withX[0];//Take the label without the ",X"
                    }

                    //Handles "#" Immediate:=====================================================================================================>
                    else if(instLine[2].contains("#")){
                        immediate = instLine[2].split("#");
                        instLine[2]=immediate[1];// Take the immediate without the "#"
                    }                    
                    if(Symbol_Table.containsKey(instLine[2])){
                        if(withX[1].equals("X")){
                            objCode=(InstructionSet.getKey(instLine[1])+UpdateIndexedAddress(toFour(Integer.toHexString(Symbol_Table.get(withX[0]))))).toUpperCase();/*****NOTE */
                            withX[1]="";
                        }
                        else
                        objCode=(InstructionSet.getKey(instLine[1])+toFour(Integer.toHexString(Symbol_Table.get(instLine[2])))).toUpperCase();/*****NOTE */
                    }
                    else if(instLine[2].matches("\\d+")){
                        objCode=(toTwo(UpdateImmopcd(InstructionSet.getKey(instLine[1])))
                        +toFour(Integer.toHexString(Integer.parseInt(instLine[2])))).toUpperCase();
                    }
                    else{
                        objCode=InstructionSet.getKey(instLine[1])+"0000";/*****NOTE */
                        //For Forward Refrence Lables. This lable wasn't found in the Symboltable.
                        Symbol_Table.put("*"+instLine[2], LOCCTR+1);
                        if(FwdRef.containsKey(instLine[2])){
                            nodes=FwdRef.get(instLine[2]);
                            
                            nodes.add(LOCCTR+1);
                            FwdRef.put(instLine[2], nodes);
                        }
                        else{
                            ArrayList<Integer> arr = new ArrayList<>();
                            arr.add(LOCCTR+1);
                            FwdRef.put(instLine[2], arr);
                        }
                    }
                    Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                    Object_Code.add(objCode);
                }   
                else if(instLine[1].equals("WORD")){
                    Object_Code.add(toFour(Integer.toHexString(LOCCTR)));
                    Object_Code.add(toSix(Integer.toHexString(Integer.parseInt(instLine[2]))));
                }
                LOCCTR+=3; 
            }         
        }
        if(!assembly.get(1).contains("RESW")||!assembly.get(1).contains("RESB")){
            String[] firstEx=assembly.get(1).split("\\s+");
            tStarts.remove(0);
            tStarts.add(0, firstEx[0]);
        }
        String Length = Integer.toHexString((LOCCTR-3)-Integer.parseInt(topRow[2],16));
        //System.out.println(Length);
        HTE.add(0,"H"+"."+toSixString(topRow[0])+"."+toSix(topRow[2])+"."+toSix(Length));
        
        for(int x = 0;x<Object_Code.size();x+=2){
            int y=x+1;
            if(Object_Code.get(y).equals("No Object Code")){
                HTE.add(T);
                T="T^";
                x+=2;
            }
            
            if(tStarts.contains(Object_Code.get(x))){
                HTE.add(T);
                T="T^"+toSix(Object_Code.get(x))+"^"+RelocBits.get(tStarts.indexOf(Object_Code.get(x)))+"^";
            }
            if(!T.endsWith(""+Object_Code.get(y))){
                T+=Object_Code.get(y);
            }
            else{
                T+=Object_Code.get(y);
            }
            if(Symbol_Table.containsValue(Integer.parseInt(Object_Code.get(x),16))&&FwdRef.containsKey(getKey(Integer.parseInt(Object_Code.get(x),16), Symbol_Table))){
                String key = getKey(Integer.parseInt(Object_Code.get(x),16), Symbol_Table);
                ArrayList <Integer> vals = FwdRef.get(key);
                for(int h=0;h<vals.size();h++){
                    HTE.add("T^"+Integer.toHexString(vals.get(h))+"^02^"+Object_Code.get(x));
                }
            }
        }      
        for(int v=0;v<HTE.size();v++){
            if(HTE.get(v).equals("")){
                HTE.remove(v);
            }
        } 
        
        for(int s=1;s<HTE.size();s++){
            String [] xyz=HTE.get(s).split("\\^");
            //System.out.println(xyz[0]+"\t");
            if(xyz[1].length()==6){
                count=xyz[3].length()/2;
                HTE.remove(s);
                HTE.add(s, xyz[0]+"^"+xyz[1]+"^"+toTwo(Integer.toHexString(count))+"^"+xyz[2]+"^"+xyz[3]);
            }
        }  

        //ArrayListTesterINT(FwdRef.get("WRREC"));
        //ArrayListTesterINT(FwdRef.get("RDREC"));
        
        //System.out.println("\n->Relocation Bits:");
        ArrayListTester(RelocBits);
        System.out.println("\n");
        ArrayListTester(tStarts);
        ArrayListTester(Object_Code);
        //System.out.println("\n->Assembly Code:");
        //ArrayListTester(assembly);
        ArrayListTester(HTE);
        scanAssem.close();
    }
    static public String getKey(Integer val,HashMap<String,Integer> h){
        String c="didnt enter yet";
        if(h.containsValue(val)){
            for(String x:h.keySet()){
                if(h.get(x).equals(val))
                    c = x;
            }
        }
        else{
            c="not found";   
        }   
        return c;
    }
    //Method to add 8 to the first character of the Lable Address to show that it's Indexed:=====================================================>
    public static String UpdateIndexedAddress(String LableAddr){
        int firstCharacter = Integer.parseInt(LableAddr,16);
        firstCharacter+=0x8000;
        return Integer.toHexString(firstCharacter);
    }
    
    //Method to add 1 to the opcode to show that it's Immediate:=================================================================================>
    public static String UpdateImmopcd(String opcd){
        int opcdInt = Integer.parseInt(opcd, 16); // Convert opcd to integer
        opcdInt += 1; // Add 1 to the integer
        return Integer.toHexString(opcdInt); // Convert the integer back to hexadecimal
    }

    //Method to extend address to 2 digits:======================================================================================================>
    public static String toTwo(String val){
        while(val.length()<2){
            val="0"+val;
        }
        return val;
    }
    
    //Method to extend address to 3 digits:======================================================================================================>
    public static String toThree(String val){
        while(val.length()<3){
            val="0"+val;
        }
        return val;
    }

    //Method to extend address to 4 digits:======================================================================================================>
    public static String toFour(String val){
        while(val.length()<4){
            val="0"+val;
        }
        return val;
    }

    //Method to extend address to 6 digits:======================================================================================================>
    public static String toSix(String val){
        while(val.length()<6){
            val="0"+val;
        }
        return val;
    }

    //Method to extend Program name to 6 characters:=============================================================================================>
    public static String toSixString(String val){
        while(val.length()<6){
            val=val+" ";
        }
        return val;
    }

    //Method to extend address to 12 digits:=====================================================================================================>
    public static String toTwelve(String val){
        while(val.length()<12){
            val+="0";
        }
        return val;
    }

    //Method to handle instructions with no label:===============================================================================================>
    public static String[] ArrayExtend(String[]arr){
        String [] array = new String[3];
        if(arr.length==1){
            array[0]="";
            array[1]=arr[0];
            array[2]="";
        }
        else if(arr.length==2){
            array[0]="";
            array[1]=arr[0];
            array[2]=arr[1];
        }
        return array;
    }
    //Testing methods:===========================================================================================================================>
    public static void ArrayListTester(ArrayList<String> arr){
        for(int i = 0;i<arr.size();i++){
            System.out.println(arr.get(i));
        }
    }
    public static void ArrayListTesterINT(ArrayList<Integer> arr){
        for(int i = 0;i<arr.size();i++){
            System.out.println(Integer.toHexString(arr.get(i)));
        }
    }
    public static void HashMapTester(HashMap<String,Integer>hash){
        for (Map.Entry<String, Integer> entry : hash.entrySet()) {
            System.out.println(entry.getKey() + ": " + Integer.toHexString(entry.getValue()));
          }
    }
    public static void TreeMapTester(TreeMap<String,String>hash){
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
          }
    }
    public static void ArrayTester(String[] arr){
        for(int i=0;i<3;i++){
            System.out.println(arr[i]);
        }
    }
    public static String ByteHandler(String instLine){
        int i =0;
	    String value = "";	
	    while(i < instLine.length()){
		    if(instLine.charAt(i) == 'X'){
                i++;
			    continue;
		    }
		    if((instLine.charAt(i) >= 'A' && instLine.charAt(i) <= 'Z') || (instLine.charAt(i) >= '0' && instLine.charAt(i) <= '9')){
			    value += instLine.charAt(i);
		    }
		    i++;
	    }
	    return value;
    }
}
