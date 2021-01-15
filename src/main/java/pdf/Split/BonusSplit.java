package pdf.Split;

public class BonusSplit {

	private String[] bpdfPage;

	//賞与明細の年月の桁数
	private static final int YEAR_MONTH_NUMBER = 9;

	//年月検索
	private static final String[] YEAR_MONTH_MATCHES = new String[] {"年", "月"};

	//社員コードの桁数
	private static final int CODE_NUMBER = 6;

	//社員コード最小値
	private static final int CODE_MIN = 100000;


	public String bonusSplitPdf(String[] strs) {

		String ym = null;

		String code = null;

		String name = null;

		String fileName = null;

		bpdfPage = strs;

		//年月を取得
		ym = getBonusYearMonth();

		if(ym == null) {
			System.out.println("年月が取得できませんでした。");
			return null;
		}

		//コードを取得
		code = getPdfCode();

		if(code == null) {
			System.out.println("社員番号が取得できませんでした。");
			return null;
		}

		//社員名を取得
		name = getPdfName(code);

		if(name == null) {
			System.out.println("社員名が取得できませんでした。");
			return null;
		}

		fileName = ym + "_" + code + "_" + name + ".pdf";

		return fileName;


	}


	/**
	 * 賞与明細書の年月を取得
	 * @return
	 */
	private  String getBonusYearMonth() {

		String str = null;

		for(int i =0; i < bpdfPage.length; i++) {
			if(bpdfPage[i].length() >= YEAR_MONTH_NUMBER) {

				//賞与明細書の年月を取得
				str = bpdfPage[i].substring(0,YEAR_MONTH_NUMBER);
				if(yearMonth(str) != null) {
					return yearMonth(str);
				}
			}
		}
		return null;
	}



	/**
	 * 年月の文字列を取得
	 * @param str
	 * @return
	 */
	private static String yearMonth(String str) {

		String ym = null;

		//文字列に年、月があるものを検索
		for(String s : YEAR_MONTH_MATCHES) {
			if(!str.contains(s)) {
				return null;
			}
		}

		//数字以外の文字をすべて削除
		ym =  str.replaceAll("[^0-9]", "");
		//月の桁数が7桁の時、最後の行を削除,7桁より大きいとき処理失敗
		if(ym.length() == 7) {
			StringBuffer sb = new StringBuffer(ym);
			sb.setLength(sb.length() - 1);
			ym = sb.toString();
		}else if(ym.length() > 7) {
			return null;
		}

		return ym;
	}


	/**
	 * 社員番号を取得
	 * @return
	 */
	private  String getPdfCode() {

		String str = null;

		for(int i =0; i <  bpdfPage.length; i++) {
			if( bpdfPage[i].length() >= CODE_NUMBER) {

				//社員番号を取得
				str =  bpdfPage[i].substring(0,CODE_NUMBER);
				if(isNumber(str)) {
					return str;
				}
			}
		}
		return null;
	}


	/**
	 * 6桁の文字列が数字かどうか判定
	 * @param num
	 * @return
	 */
	private static  boolean isNumber(String num) {

		try {

			//社員番号が100000以上の時
			if(Integer.parseInt(num) >= CODE_MIN) {
				return true;
			}
			return false;
		}catch (NumberFormatException e) {

			return false;
		}

	}

	/**
	 * 社員名を取得
	 * @param code
	 * @return
	 */
	private String getPdfName(String code) {

		String str = null;
		String name = null;

		for(int i =0; i <  bpdfPage.length; i++) {
			if( bpdfPage[i].length() >= CODE_NUMBER) {
				str =  bpdfPage[i].substring(0,CODE_NUMBER);
				if(str.equals(code)) {

					//社員コード以降の文字列を取得
					name =  bpdfPage[i].substring(CODE_NUMBER).trim();
					return stripAll(name);
				}
			}

		}
		return null;
	}

	/**
	 * 姓名の間の全角スペースを削除
	 * @param str
	 * @return
	 */
	private static  String stripAll(String str) {

		if(str == null || str.isEmpty()) {
			return str;
		}
		return str.replaceAll("　", "");
	}

}
