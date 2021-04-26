public class Jp{

	public static void main(String[] args){

		System.out.println("Integer string evaluation");
		Calc a = new Calc("1+2*3^4");
		System.out.println(a.ans());

	}


}

class Calc{

	private String str;
	private final char[] char_stack;
	private int[] int_stack;
	private int top = 0;
	private final StringBuilder post = new StringBuilder();
	int ans = 0;

	Calc(String exp){
		str = exp;
		char_stack = new char[str.length()];
		int alpha_count = 0;
		for(int i=0; i<str.length(); i++){
			if(Character.isLetter(str.charAt(i)))
				alpha_count++;
		}

		if(alpha_count == 0){
			postfix();
			eval();
		}else{
			System.out.println("Enter only digits not alphabets");
		}
	}

	private void char_push(char x){
		char_stack[++top] = x;
	}

	private char char_pop(){
		return char_stack[top--];
	}

	private void int_push(int x){
		int_stack[++top] = x;
	}

	private int int_pop(){
		return int_stack[top--];
	}

	private int priority(char x){
		if(x == '(')
			return 0;
		if(x == '+' || x == '-')
			return 1;
		if(x == '*' || x == '/')
			return 2;
		if(x == '^')
			return 3;
		else
			return 0;
	}

	private void postfix(){
		char temp, x;

		for(int i=0; i<str.length(); i++){
			temp = str.charAt(i);

			if(Character.isLetterOrDigit(temp)){
				post.append(temp);
			}else if(temp == '('){
				char_push(temp);
			}else if(temp == ')'){
				while((x = char_pop()) != '(')
					post.append(x);
			}else{
				while(priority(char_stack[top]) >= priority(temp)){
					post.append(char_pop());
				}
				char_push(temp);
			}
		}

		while(top != -1){
			post.append(char_pop());
		}

	}

	private void eval(){
		str = post.toString();
		int_stack = new int[str.length()];
		char temp;
		top = 0;
		int a, b;

		for(int i=0; i < str.length(); i++){
			temp = str.charAt(i);

			if(Character.isDigit(temp)){
				// ASCII Values of a char number - char 0 gives the actual number value
				int_push(temp - '0');
			}else if(temp == '+' || temp == '-' || temp == '*' || temp == '/' || temp == '^'){
				b = int_pop();
				a = int_pop();

				switch(temp){
					case '+':
						ans = a + b;
						break;
					case '-':
						ans = a - b;
						break;
					case '*':
						ans = a * b;
						break;
					case '/':
						ans = a / b;
						break;
					case '^':
						ans = (int)Math.pow(a, b);
						break;
				}

				int_push(ans);

			}

		}

		ans = int_pop();

	}

	public int ans(){
		return ans;
	}

}