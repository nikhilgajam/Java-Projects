public class Jp{

    public static void main(String[] args){

        System.out.println("StringCal Will Evaluate The String With Two Operands\n");

        StringEval s = new StringEval();
        s.setEval("99+1");
        System.out.println(s.getEval());

    }

}

class StringEval{

    /**
     * This StringEval will evaluate the string with only two operands
     **/


    private String eval;
    private int add=0, sub=0, mul=0, div=0, mod=0, end=0;

    // To set the string
    public void setEval(String str){
        eval = str;
        end = eval.length();
        string_eval();
    }

    private void string_eval(){

            add = eval.indexOf('+', add);
            sub = eval.indexOf('-', sub);
            mul = eval.indexOf('*', mul);
            div = eval.indexOf('/', div);
            mod = eval.indexOf('%', mod);

            if(add != -1){
                adds(eval);
            }else if(sub != -1){
                subs(eval);
            }else if(mul != -1){
                muls(eval);
            }else if(div != -1){
                divs(eval);
            }else if(mod != -1){
                mods(eval);
            }

    }

    private void adds(String x){
        String a = x.substring(0, add);
        String b = x.substring(add+1, end);

        double c = Double.parseDouble(a) + Double.parseDouble(b);
        eval =  c + "";
    }

    private void subs(String x){
        String a = x.substring(0, sub);
        String b = x.substring(sub+1, end);

        double c = Double.parseDouble(a) - Double.parseDouble(b);
        eval =  c + "";
    }

    private void muls(String x){
        String a = x.substring(0, mul);
        String b = x.substring(mul+1, end);

        double c = Double.parseDouble(a) * Double.parseDouble(b);
        eval =  c + "";
    }

    private void divs(String x){
        String a = x.substring(0, div);
        String b = x.substring(div+1, end);

        double c = Double.parseDouble(a) / Double.parseDouble(b);
        eval = c + "";
    }

    private void mods(String x){
        String a = x.substring(0, mod);
        String b = x.substring(mod+1, end);

        double c = Double.parseDouble(a) % Double.parseDouble(b);
        eval = c + "";
    }

    // To get the evaluated string
    public String getEval(){
        return eval;
    }

}