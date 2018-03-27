package formatters;

public class ConadFactory {
	public static ConadParser getNewConadParser(Integer tipoConad){
		if (tipoConad.equals(1)){
			return new Conad1();
		} else {
			return new Conad2();
		}
	}
}
