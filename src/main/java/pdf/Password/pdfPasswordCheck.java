package pdf.Password;

import java.io.File;

public class pdfPasswordCheck {

	/**
	 * パスワードを取得する
	 * @param filePaths 各ファイルパス
	 * @return　パスワード
	 */
	public String pdfPassCheck(String[] filePaths) {

		String pass = "";

		for( String path : filePaths) {

			File pdfFile = new File(path);

			//引数がファイル及びフォルダでないとき
			if(!pdfFile.isFile() && !pdfFile.isDirectory()) {
				pass = isPassWordCheck(path);
			}
		}
		return pass;
	}

	/**
	 * password=を判定する
	 * @param pass
	 * @return
	 */
	private String isPassWordCheck(String pass) {

		String passResult = "";

		if(pass.contains("password=")) {
			passResult = pass.replace("password=", "");


			if(passResult.equals("")) {
				System.out.println("password= はエラーになります。パスワードを設定しない場合は引数に指定しないでください。");
				System.out.println("処理を終了します。");
				System.exit(0);
			}

		}else {
			System.out.println("パスワードはpassword=○○○○の形式で設定してください");
			System.out.println("処理を終了します。");
			System.exit(0);
		}

		return passResult;
	}

}
