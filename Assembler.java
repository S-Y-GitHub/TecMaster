import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assembler{
    public static Object[] assenmble(String data)throws AssembleException{
        String[] rawLines=data.split("\n");
        String[] lines=rawLines.clone();
        for(int i=0;i<lines.length;i++){
            String line=lines[i];
            String fixed="";
            for(int j=0;j<line.length();j++){
                fixed+=String.valueOf(line.charAt(j)).toUpperCase();
                char c=line.charAt(j);
                if(c=='\''){
                    fixed+=line.charAt(++j)+'\'';
                }else if(c=='\"'){
                    do{
                        fixed+=line.charAt(++j);
                    }while(line.charAt(j)!='\"');
                }else if(c==';'){
                    while(++j<line.length()){
                        fixed+=line.charAt(j);
                    }
                }
            }
            lines[i]=fixed;
        }

        Map<String,Byte> labels;
        try{
            labels=getLabels(lines);
        }catch(AssembleException e){
            throw new AssembleException(e.line,rawLines[e.line],e.message);
        }
        
        System.out.println("----------labels----------");
        for(String key:labels.keySet()){
            System.out.println(String.format("%-8s : %2s",key,to2DigitHexString(labels.get(key))));
        }

        Object[] assembled;
        try{
            assembled=assemble(lines,labels);
        }catch(AssembleException e){
            throw new AssembleException(e.line,rawLines[e.line],e.message);
        }
        byte[] binary=(byte[])assembled[0];
        String list=(String)assembled[1];

        System.out.println("----------binary----------");
        System.out.println(String.format("load-address  : %2s",to2DigitHexString(binary[0])));
        System.out.println(String.format("program-length: %2s",to2DigitHexString(binary[1])));
        for(byte b=2;b<binary.length;b++){
            System.out.println(String.format("%2s : %2s",to2DigitHexString((byte)(b-2)),to2DigitHexString(binary[b])));
        }

        return new Object[]{binary,list};
    }
    public static Map<String,Byte> getLabels(String[] data)throws AssembleException{
        byte add=0x00;
        Map<String,Byte> labels=new HashMap<>();
        for(int i=0;i<data.length;i++){
            try{
                if(isCommandLine(data[i])){
                    String line=data[i];
                    int index=line.indexOf(';');
                    if(index!=-1){
                        line=line.substring(0,index);
                    }
                    String[] split=line.split("[\s]+");
                    String label=split[0];
                    String command=split[1];
                    String operand=split.length==3?split[2]:null;
    
                    if(label.matches("^[A-Z].*")){
                        if(split[1].equals("EQU")){
                            labels.put(label,readFormula(operand,labels));
                        }else{
                            labels.put(label,add);
                        }
                    }
    
                    if(isTwoByteCommand(command)){
                        add+=2;
                    }else if(command.equals("DS")){
                        add+=readFormula(operand,labels);
                    }else if(command.equals("DC")){
                        add+=readValueColumn(operand,labels).length;
                    }else if(command.equals("ORG")){
                        add=readFormula(operand,labels);
                    }else if(!command.equals("EQU")){
                        add++;
                    }
                }
            }catch(Exception e){
                throw new AssembleException(i,e.getMessage());
            }
        }
        return labels;
    }
    private static boolean isCommandLine(String line){
        return !(line.matches("[\s]*")||line.trim().startsWith(";"));
    }

    final static byte ZERO=(byte)0x00;
    private static Object[] assemble(String[] data,Map<String,Byte> labels)throws AssembleException{
        byte add=0x00;

        List<Byte> rawBinary=new ArrayList<>();
        rawBinary.add(ZERO);

        ListGenerator listGen=new ListGenerator();

        for(int i=0;i<data.length;i++){
            try{
                if(isCommandLine(data[i])){
                    String line=data[i];
                    String comment="";
                    int index=line.indexOf(';');
                    if(index!=-1){
                        comment=line.substring(index);
                        line=line.substring(0,index);
                    }
                    String[] split=line.split("[\s]+");
                    String label=split[0];
                    String command=split[1];
                    String operand=split.length==3?split[2]:"";
    
                    listGen.setAddress(add);
                    listGen.setLabel(label);
                    listGen.setCommand(command);
                    listGen.setOperand(operand);
                    listGen.setComment(comment);
                    if(command.equals("EQU")){
                        listGen.setAddress(labels.get(label));
                    }else if(command.equals("ORG")){
                        byte org=readFormula(operand,labels);
                        if(add==0x00){
                            rawBinary.set(0,org);
                        }else{
                            while(add<org){
                                rawBinary.add(ZERO);
                            }
                        }
                        add=readFormula(operand,labels);
                        listGen.setAddress(add);
                    }else if(command.equals("DS")){
                        byte size=readFormula(operand,labels);
                        for(byte b=0;b<size;b++){
                            rawBinary.add(ZERO);
                            listGen.addCode(ZERO);
                            add++;
                        }
                    }else if(command.equals("DC")){
                        byte[] constants=readValueColumn(operand,labels);
                        for(byte constant:constants){
                            rawBinary.add(constant);
                            listGen.addCode(constant);
                        }
                    }else{
                        byte firstByte=firstByte(command,operand);
                        rawBinary.add(firstByte);
                        listGen.addCode(firstByte);
                        add++;
                    }
                    boolean isTwoByteCode=isTwoByteCommand(command);
                    if(isTwoByteCode){
                        byte secondByte=secondByte(command,operand,labels);
                        rawBinary.add(secondByte);
                        listGen.addCode(secondByte);
                        add++;
                    }
                    listGen.nextLine();
                }else{
                    listGen.nextLine(add,data[i]);
                }
            }catch(Exception e){
                throw new AssembleException(i,e.getMessage());
            }
        }

        rawBinary.add(1,(byte)(rawBinary.size()-1));

        int length=rawBinary.size();
        byte[] binary=new byte[length];
        for(int i=0;i<length;i++){
            binary[i]=rawBinary.get(i);
        }

        return new Object[]{binary,listGen.getList()};
    }
    static class ListGenerator{
        List<String> list=new ArrayList<>();
        ListGenerator(){
            initVar();
            this.add=0;
            this.list.add("ADR CODE    Label   Instruction\n");
        }
        private void initVar(){
            this.label="";
            this.command="";
            this.operand="";
            this.comment="";
            this.code1=null;
            this.code2=null;
        }
        byte add;
        public void setAddress(byte add){
            this.add=add;
        }
        String label;
        public void setLabel(String label){
            this.label=label;
        }
        String command;
        public void setCommand(String command){
            this.command=command;
        }
        String operand;
        public void setOperand(String operand){
            this.operand=operand;
        }
        String comment;
        public void setComment(String comment){
            this.comment=comment;
        }
        String code1,code2;
        public void addCode(byte code){
            if(code1==null){
                this.code1=to2DigitHexString(code);
            }else if(code2==null){
                this.code2=to2DigitHexString(code);
            }else{
                nextLine();
                this.code1=to2DigitHexString(code);
            }
        }
        public void nextLine(){
            if(code2==null){
                list.add(String.format("%-4s%-8s%-8s%-8s%-12s",to2DigitHexString(add),code1==null?"":code1,label,command,operand)+comment);
                add++;
            }else{
                list.add(String.format("%-4s%-3s%-5s%-8s%-8s%-12s",to2DigitHexString(add),code1==null?"":code1,code2==null?"":code2,label,command,operand)+comment);
                add+=2;
            }
            initVar();
        }
        public void nextLine(byte add,String comment){
            list.add(String.format("%-12s",to2DigitHexString(add))+comment);
        }
        public String getList(){
            String list="";
            for(String l:this.list){
                list+="\n"+l;
            }
            return list;
        }
    }
    private static byte firstByte(String command,String operand)throws Exception{
        byte b;
        switch(command){
            case "NO":{
                return 0x00;
            }
            case "LD":{
                b=0x10;
                break;
            }
            case "ST":{
                b=0x20;
                break;
            }
            case "ADD":{
                b=0x30;
                break;
            }
            case "SUB":{
                b=0x40;
                break;
            }
            case "CMP":{
                b=0x50;
                break;
            }
            case "AND":{
                b=0x60;
                break;
            }
            case "OR":{
                b=0x70;
                break;
            }
            case "XOR":{
                b=(byte)0x80;
                break;
            }
            case "SHLA":{
                b=(byte)0x90;
                break;
            }
            case "SHLL":{
                b=(byte)0x91;
                break;
            }
            case "SHRA":{
                b=(byte)0x92;
                break;
            }
            case "SHRL":{
                b=(byte)0x93;
                break;
            }
            case "JMP":{
                b=(byte)0xa0;
                break;
            }
            case "JZ":{
                b=(byte)0xa4;
                break;
            }
            case "JC":{
                b=(byte)0xa8;
                break;
            }
            case "JM":{
                b=(byte)0xac;
                break;
            }
            case "CALL":{
                b=(byte)0xb0;
                break;
            }
            case "JNZ":{
                b=(byte)0xb4;
                break;
            }
            case "JNC":{
                b=(byte)0xb8;
                break;
            }
            case "JNM":{
                b=(byte)0xbc;
                break;
            }
            case "IN":{
                b=(byte)0xc0;
                break;
            }
            case "OUT":{
                b=(byte)0xc3;
                break;
            }
            case "PUSH":{
                b=(byte)0xd0;
                break;
            }
            case "POP":{
                b=(byte)0xd2;
                break;
            }

            //COMMAND_1
            case "PUSHF":return (byte)0xdd;
            case "POPF":return (byte)0xdf;
            case "EI":return (byte)0xe0;
            case "DI":return (byte)0xe3;
            case "RET":return (byte)0xec;
            case "RETI":return (byte)0xef;
            case "HALT":return (byte)0xff;

            default:{
                throw new Exception("不明な命令 : "+command);
            }
        }

        if(isCommand2(command)||isCommand3(command)){
            operand=operand.split(",")[0];
            switch(operand){
                case "G0":return (byte)(b+0b0000);
                case "G1":return (byte)(b+0b0100);
                case "G2":return (byte)(b+0b1000);
                case "SP":return (byte)(b+0b1100);
                default:throw new Exception("不明なオペランド : "+operand);
            }
        }
        if(isCommand4(command)||isCommand5(command)){
            String[] split=operand.split(",");
            String register=split[0];
            String formula=split[1];
            switch(register){
                case "G0":{
                    b+=0b0000;
                    break;
                }
                case "G1":{
                    b+=0b0100;
                    break;
                }
                case "G2":{
                    b+=0b1000;
                    break;
                }
                case "SP":{
                    b+=0b1100;
                    break;
                }
                default:{
                    throw new Exception("不明なレジスタ : "+register);
                }
            }
            if(split.length==3){
                if(isCommand5(command)){
                    throw new Exception(command+"命令ではイミディエイトモードを使用できません");
                }
                String indexRegister=split[2];
                if(indexRegister.equals("G1")){
                    b+=0x01;
                }else if(indexRegister.equals("G2")){
                    b+=0x02;
                }else{
                    throw new Exception("許可されていない、または不明なインデクスレジスタ : "+indexRegister);
                }
            }else if(formula.startsWith("#")){
                b+=0x03;
            }
            return b;
        }
        if(isCommand6(command)){
            String[] split=operand.split(",");
            if(split.length==2){
                String indexRegister=split[1];
                if(indexRegister.equals("G1")){
                    b+=0x01;
                }else if(indexRegister.equals("G2")){
                    b+=0x02;
                }else{
                    throw new Exception("許可されていない、または不明なインデクスレジスタ : "+indexRegister);
                }
            }
            return b;
        }
        throw new Exception("this is bug");
    }
    private static byte secondByte(String command,String operand,Map<String,Byte> labels)throws Exception{
        String[] split=operand.split(",");
        String formula;
        if(split.length==1){
            formula=split[0];
        }else if(split.length==2){
            if(isCommand6(command)){
                formula=split[0];
            }else{
                formula=split[1];
                if(formula.startsWith("#")){
                    formula=formula.substring(1);
                }
            }
        }else if(split.length==3){
            formula=split[1];
        }else{
            throw new Exception("不明なオペランド : "+operand);
        }
        return readFormula(formula,labels);
    }

    final static String[] COMMAND_1={
        "NO","PUSHF","POPF","EI","DI","RET","RETI","HALT"
    };
    final static String[] COMMAND_2={
        "SHLA","SHLL","SHRA","SHRL","PUSH","POP"
    };
    final static String[] COMMAND_3={
        "IN","OUT"
    };
    final static String[] COMMAND_4={
        "LD","ADD","SUB","CMP","AND","OR","XOR"
    };
    final static String[] COMMAND_5={
        "ST"
    };
    final static String[] COMMAND_6={
        "JMP","JZ","JC","JM","JNZ","JNC","JNM","CALL"
    };
    @SuppressWarnings("unused")
    private static boolean isCommand1(String command){
        for(String command1:COMMAND_1){
            if(command.equals(command1)){
                return true;
            }
        }
        return false;
    }
    private static boolean isCommand2(String command){
        for(String command2:COMMAND_2){
            if(command.equals(command2)){
                return true;
            }
        }
        return false;
    }
    private static boolean isCommand3(String command){
        for(String command3:COMMAND_3){
            if(command.equals(command3)){
                return true;
            }
        }
        return false;
    }
    private static boolean isCommand4(String command){
        for(String command4:COMMAND_4){
            if(command.equals(command4)){
                return true;
            }
        }
        return false;
    }
    private static boolean isCommand5(String command){
        for(String command5:COMMAND_5){
            if(command.equals(command5)){
                return true;
            }
        }
        return false;
    }
    private static boolean isCommand6(String command){
        for(String command6:COMMAND_6){
            if(command.equals(command6)){
                return true;
            }
        }
        return false;
    }
    private static boolean isTwoByteCommand(String command){
        return isCommand3(command)||isCommand4(command)||isCommand5(command)||isCommand6(command);
    }

    private static byte[] readValueColumn(String valueString,Map<String,Byte> labels)throws Exception{
        String[] valueStrings=valueString.split(",");
        List<Byte> rawValues=new ArrayList<>();
        for(int i=0;i<valueStrings.length;i++){
            for(byte value:readExtendedFormula(valueStrings[i],labels)){
                rawValues.add(value);
            }
        }
        int length=rawValues.size();
        byte[] values=new byte[length];
        for(int i=0;i<length;i++){
            values[i]=rawValues.get(i);
        }
        return values;
    }
    private static byte[] readExtendedFormula(String eFormula,Map<String,Byte> labels)throws Exception{
        if(eFormula.matches("\".*\"")){
            List<Byte> rawValues=new ArrayList<>();
            for(int i=1;i<eFormula.length()-1;i++){
                rawValues.add((byte)eFormula.charAt(i));
            }
            int length=rawValues.size();
            byte[] values=new byte[length];
            for(int i=0;i<length;i++){
                values[i]=rawValues.get(i);
            }
            return values;
        }else{
            return new byte[]{readFormula(eFormula,labels)};
        }
    }
    private static byte readFormula(String formula,Map<String,Byte> labels)throws Exception{
        int index;
        index=formula.indexOf("(");
        if(index!=-1){
            int postIndex=formula.indexOf(")");
            if(formula.indexOf(")")==-1){
                throw new Exception("閉じられていない括弧 : "+formula);
            }
            String pre=formula.substring(0,index);
            String parent=formula.substring(index+1,postIndex);
            String post=formula.substring(postIndex+1);
            return readFormula(pre+readFormula(parent,labels)+post,labels);
        }

        index=formula.indexOf("+"); 
        if(index==0){
            String form=formula.substring(1);
            return readFormula(form,labels);
        }else if(index>0){
            String form1=formula.substring(0,index);
            String form2=formula.substring(index+1);
            return (byte)(readFormula(form1,labels)+readFormula(form2,labels));
        }

        index=formula.indexOf("-");
        if(index==0){
            String form=formula.substring(1);
            return (byte)(-1*readFormula(form,labels));
        }else if(index>0){
            String form1=formula.substring(0,index);
            String form2=formula.substring(index+1);
            return (byte)(readFormula(form1,labels)-readFormula(form2,labels));
        }
        return readParagraph(formula,labels);
    }
    private static byte readParagraph(String paragraph,Map<String,Byte> labels)throws Exception{
        int index;
        index=paragraph.indexOf("*");
        if(index!=-1){
            String parag1=paragraph.substring(0,index);
            String parag2=paragraph.substring(index+1);
            return (byte)(readParagraph(parag1,labels)*readParagraph(parag2,labels));
        }
        index=paragraph.indexOf("/");
        if(index!=-1){
            String parag1=paragraph.substring(0,index);
            String parag2=paragraph.substring(index+1);
            return (byte)(readParagraph(parag1,labels)/readParagraph(parag2,labels));
        }
        return readFactor(paragraph,labels);
    }
    private static byte readFactor(String factor,Map<String,Byte> labels)throws Exception{
        if(factor.matches("'.'")){
            return (byte)factor.charAt(1);
        }else if(factor.matches("^[0-9].*")){
            if(factor.endsWith("H")){
                return (byte)Integer.parseInt(factor.replace("H",""),16);
            }else{
                return (byte)Integer.parseInt(factor);
            }
        }else{
            Byte label=labels.get(factor);
            if(label==null){
                throw new Exception("不明なラベル : "+factor);
            }
            return labels.get(factor);
        }
    }

    final static String[] HEX={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    public static String to2DigitHexString(byte b){
        return HEX[Byte.toUnsignedInt(b)>>>4]+HEX[Byte.toUnsignedInt(b)&0b0000_1111];
    }

    public static class AssembleException extends Exception{
        int line;
        String lineStr;
        String message;
        public AssembleException(int line,String message){
            this.line=line;
            this.message=message;
        }
        public AssembleException(int line,String lineStr,String message){
            System.err.println((this.line=line)+"行目");
            System.err.println(this.lineStr=lineStr);
            System.err.println(this.message=message);
        }
    }
}