package pdf.Merge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;



public class pdfMergeProcess {

//	//年月の要素数
//	private static final int YEAR_MONTH_NUMBER = 0;
//
//	//コードの要素数
//	private static final int EMPLOYEE_CODE_NUMBER = 1;

	private static final String PAYMENT_DETAILS = "支払明細";


	/**
	 * ファイル名から【】を削除
	 * @param fileName pdfファイル名
	 * @return name 【】を削除したファイル名
	 */
	public String pdfMergeGetName(String fileName) {

		String name = null;

		int index = 0;

		//】までの文字列を位置を取得
		index = fileName.indexOf("】");

		//【】以降の文字列を取得
		name = fileName.substring(index + 1);

		return name ;
	}


	/**
	 * pdfファイルが結合可能か判定する
	 * @param filePaths　各フォルダパス
	 * @param mergePath　支払明細パス
	 * @param pass　パスワード
	 */
	public void pdfFileMerge(String[] filePaths ,String mergePath, String pass) {

		String[] fileNames = null;

		String ymCode = null;

		fileNames = pdfFileList(filePaths[0]);

		for(String fileName : fileNames) {
			ymCode = pdfFileNameGetYearMonthCode(fileName);

			if(!isYearMonthCodeMatch(ymCode, filePaths, mergePath, pass)) {
				errPdfMessege(ymCode, filePaths);
			}

		}

//		ArrayList<String[]> list = null;
//
//		for(int i = 0; i < FILE_LIST.size() - 1; i++) {
//
//			//年月、コード、名前を取得
//			list = pdfMergeGetList(FILE_LIST.get(i));
//
//			for(int j = 0; j < list.size(); j++) {
//
//				// 給与、賞与、振込のファイル名から同じ年月、コードを検索
//				if(isYearMonthCodeMatch(list.get(j) , i)){
//
//					String[] mergeName = new String[FILE_LIST.size()];
//
//					//結合させるファイル名を取得する
//					for(int k = 0; k < FILE_LIST.size() ; k++) {
//						mergeName[k] = FILE_LIST.get(k)[j];
//
//					}
//
//					// 給与、賞与、振込の年月、コードごとに結合させる。パスワードが指定されていたら設定する。
//					pdfMergeFileCreate(paths, mergeName , pass);
//
//				}
//			}
//		}
	}

	/**
	 *　支払明細ファイルを作成する
	 * @param mergeFiles　結合するファイル群(給与、賞与、振込)
	 * @param mergePath　支払明細パス
	 * @param pass　パスワード
	 */
	private void pdfMergeFileCreate(ArrayList<String> mergeFiles, String mergePath , String pass) {

		String inputName = null;
		//List<InputStream> list = new ArrayList<InputStream>();
		String mergeFile = null;


		// 支払明細のファイルパスを取得
		mergeFile = mergePath + "\\" + "【" + PAYMENT_DETAILS + "】"+  pdfMergeGetName(mergeFiles.get(0));

		PDFMergerUtility pdfMerger = new PDFMergerUtility();

		try {

			//結合させるファイルを取得
			for(int i = 0; i < mergeFiles.size(); i++) {

				inputName = mergeFiles.get(i);

				//結合させるファイルを入れる
				pdfMerger.addSource(new File(inputName));

			}

			try(FileOutputStream mergedPDFOutputStream =  new FileOutputStream(mergeFile);){

				pdfMerger.setDestinationStream(mergedPDFOutputStream);
				//PDFのMerge出力
				pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());


			}
			//パスワードが空白でないとき実行
			if(!pass.equals("")) {
				pdfMergeSetPassWord(mergeFile, pass);
			}

			System.out.println(new File(mergeFile).getName() + " の結合に成功しました。");

		}catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 支払明細のファイルパスを取得する
	 * @param path　第一引数のフォルダパス
	 * @return filePath 支払明細のフォルダパス
	 */
	public String getSaveFolder(String path) {

		String paymentPath = null;
		//支払明細のファイルパスを取得
		paymentPath = FilenameUtils.getFullPath(path) + PAYMENT_DETAILS;

		return paymentPath;


	}

	/**
	 * 各フォルダ内のファイルからYYYYMM_XXXXXXのファイルがあるか判定する
	 * @param ymCode 年月_コード(YYYYMM_XXXXXX)
	 * @param filePaths　各フォルダパス
	 * @param mergePath　支払明細パス
	 * @param pass　パスワード
	 * @return　true/false
	 */
	public Boolean isYearMonthCodeMatch(String ymCode, String[] filePaths, String mergePath, String pass) {

		String[] fileNames = null;
		String mergeFilePath = null;
		ArrayList<String> mergeFiles = new ArrayList<String>();
		int count = 0;

		for(String filePath : filePaths) {

			fileNames = pdfFileList(filePath);

			for(String fileName : fileNames) {

				if(fileName.contains(ymCode)) {
					mergeFilePath = filePath + "\\" + fileName;
					mergeFiles.add(mergeFilePath);
					count++;
					break;
				}
			}


		}
		if(count == filePaths.length) {
			pdfMergeFileCreate(mergeFiles, mergePath, pass) ;

			return true;

		}else {

			return false;
		}



//		ArrayList<String[]> list = null;
//
//		int count = 0;
//
//		num = num + 1;
//
//		// FILE_LISTの次の要素から開始
//
//		num = num + 1;
//
//		for(int i = num; i < FILE_LIST.size(); i++) {
//			list = pdfMergeGetList(FILE_LIST.get(i));
//
//			for(int j = 0; j < list.size(); j++) {
//
//				//各フォルダに同じ年月、コードのファイルが存在する時countを１つ増やす
//				if(strs[YEAR_MONTH_NUMBER].equals(list.get(j)[YEAR_MONTH_NUMBER]) && strs[EMPLOYEE_CODE_NUMBER].equals(list.get(j)[EMPLOYEE_CODE_NUMBER]) ) {
//
//					count++;
//
//					//　各フォルダに同じ年月、コードのファイルがあるときtrueを返す
//					if( count == (FILE_LIST.size() - 1)) {
//						return true;
//					}
//
//					break;
//				}
//
//				// 各フォルダに同じ年月、コードのファイルがないときfalseを返す
//				if(j == list.size() - 1) {
//
//					return false;
//				}
//
//			}
//
//		}
//		return false;
	}



	/**
	 * 支払明細PDFファイルにパスワードを設定する
	 * @param fileName　支払明細PDFファイルパス
	 * @param pass　パスワード
	 */
	private void pdfMergeSetPassWord(String filePath, String pass) {

		//PDFドキュメントの読み込み
		File file = new File(filePath);

		try(PDDocument document = PDDocument.load(file);){
			//アクセス許可オブジェクトの作成
			AccessPermission accessPermission = new AccessPermission();

			//StandardProtectionPolicyオブジェクトの作成
			StandardProtectionPolicy spp = new StandardProtectionPolicy(pass, pass, accessPermission);

			//暗号化キーの長さを設定
			spp.setEncryptionKeyLength(128);

			//許可の設定
			spp.setPermissions(accessPermission);

			//文書の保護
			document.protect(spp);

			//保存する
			document.save(filePath);

		} catch (IOException e) {
			e.printStackTrace();
		}

		//パスワードが設定されているか確認する
		pdfMergeFilePassCheck(file, pass);

	}

	/**
	 * 支払明細PDFファイルにパスワードが設定されているかチェック
	 * @param filePath 支払明細PDFファイルパス
	 * @param pass パスワード
	 */
	private void pdfMergeFilePassCheck(File filePath, String pass) {

		try (PDDocument document = PDDocument.load(filePath, pass);){

			//暗号化していないとき
			if(!document.isEncrypted()) {
				System.out.println(filePath + "の暗号化に失敗しました。");
			}

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * フォルダ内の全ファイル名を取得する
	 * @param filePath　フォルダパス
	 * @return　フォルダ内の全ファイル名
	 */
	private String[] pdfFileList(String filePath){


		File file = new File(filePath);
		String[] fileList = file.list();

		return fileList;

	}

	/**
	 * 結語失敗時のエラーメッセージを表示する
	 * @param ymCode 年月_コード(YYYYMM_XXXXXX)
	 * @param filePaths 各フォルダパス
	 */
	private void errPdfMessege(String ymCode, String[] filePaths) {

		String[] fileNames = null;

		String folderName = null;

		String paymentName = null;

		int count;

		for(int i = 0; i < filePaths.length; i++) {

			count = 0;

			//フォルダ内のファイル名を取得
			fileNames = pdfFileList(filePaths[i]);

			//ファイル名からYYYYMM_XXXXXXを検索
			for(String fileName : fileNames) {
				if(fileName.contains(ymCode)) {
					paymentName = fileName;
				}else {
					count++;
				}

			}

			if(count == fileNames.length) {
				folderName = getPdfFilePath(filePaths[i], ymCode);

				System.out.println(folderName + "フォルダに" + ymCode + "と同じファイル名がありません。");

			}

		}

		System.out.println(getPdfFilePaymentName(paymentName) + "の結合に失敗しました。");

	}

	/**
	 * ファイルパスを取得する
	 * @param filePath　ファイルパス
	 * @param ymCode　年月_コード(YYYYMM_XXXXXX)
	 * @return　ファイル名
	 */
	private String getPdfFilePath(String filePath, String ymCode) {

		int index = 0;

		String fileName = null;

		index = filePath.lastIndexOf("\\");

		fileName = filePath.substring(index + 1);

		return fileName ;
	}

	/**
	 * ファイル名を【支払明細】のファイル名に変更する
	 * @param fileName　ファイル名
	 * @return　【支払明細】ファイル名
	 */
	private String getPdfFilePaymentName(String fileName) {

		String name = null;

		int index = 0;

		//】までの文字列を位置を取得
		index = fileName.indexOf("】");

		name = "【支払明細】" + fileName.substring(index + 1);

		return name;
	}

	/**
	 * ファイル名から年月_コード(YYYYMM_XXXXXX)を取得する
	 * @param filePath　ファイルパス
	 * @return
	 */
	private String pdfFileNameGetYearMonthCode(String filePath) {

		String name = null;

		int index = 0;

		//】までの文字列の位置を取得
		index = filePath.indexOf("】");

		//YYYYMM_XXXXXX以降の文字列を取得
		name = filePath.substring(index + 1, index + 14);

		return name ;
	}

}
