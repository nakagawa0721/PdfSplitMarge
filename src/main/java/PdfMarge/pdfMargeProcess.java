package PdfMarge;

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


public class pdfMargeProcess {

	//給与、賞与、振込明細のフォルダ内のファイルを追加するList
	ArrayList<String[]> FILE_LIST = new ArrayList<String[]>();

	//ArrayList<String[]> list = new ArrayList<String[]>();

	//年月の要素数
	private int YEAR_MANTH_NUMBER = 0;

	//コードの要素数
	private int EMPLOYEE_CODE_NUMBER = 1;

	private String PAYMENT_DETAILS = "支払明細";


	/**
	 * ファイル名から【】を削除
	 * @param fileName pdfファイル名
	 * @return name 【】削除したファイル名
	 */
	public String pdfMargeGetName(String fileName) {

		String name = null;

		int index = 0;

		//】までの文字列を位置を取得
		index = fileName.indexOf("】");

		//【】以降の文字列を取得
		name = fileName.substring(index + 1);

		return name ;
	}


	/**
	 *	各フォルダに入ってあるpdfファイル名をListに全て入れる
	 * @param paths　各フォルダパス
	 */
	public void pdfFileNameSetList(String[] paths) {


//		for(int i = 0; i < paths.length ; i++) {
//			File f = new File(paths[i]);
//			//ファイルパス内のファイル名を配列に入れる
//			String[] fList = f.list();
//			//配列をListに入れる
//			FILE_LIST.add(fList);
//
//		}

		for(String p : paths) {
			File f = new File(p);
			//ファイルパス内のファイル名を配列に入れる
			String[] fList = f.list();
			//配列をListに入れる
			FILE_LIST.add(fList);
		}

	}

	/**
	 *各フォルダの給与、賞与、振込ファイルから同じ年月、コードを検索する
	 * @param paths 各フォルダパス
	 * @param pass パスワード
	 */
	public void pdfFileMager(String[] paths , String pass) {

		ArrayList<String[]> list = null;


		for(int i = 0; i < FILE_LIST.size() - 1; i++) {

			//年月、コード、名前を取得
			list = pdfMargeGetList(FILE_LIST.get(i));

			for(int j = 0; j < list.size(); j++) {

				// 給与、賞与、振込のファイル名から同じ年月、コードを検索
				if(isYearMonthCodeMatch(list.get(j) , i)){

					String[] margeName = new String[FILE_LIST.size()];

					//結合させるファイル名を取得する
					for(int k = 0; k < FILE_LIST.size() ; k++) {
						margeName[k] = FILE_LIST.get(k)[j];

					}

					// 給与、賞与、振込の年月、コードごとに結合させる。パスワードが指定されていたら設定する。
					pdfMargeFileCreate(paths, margeName , pass);

				}
			}
		}
	}


	/**
	 * 給与、賞与、振込の年月、コードごと支払明細pdfを作成する
	 * @param paths　各フォルダパス
	 * @param margeName
	 */
	private void pdfMargeFileCreate(String[] paths, String[] margeName,String pass) {

		String inputName = null;
		//List<InputStream> list = new ArrayList<InputStream>();
		String margeFile = null;

		// 支払明細のファイルパスを取得
		margeFile = getSaveFolder(paths[0]) + "\\" + "【" + PAYMENT_DETAILS + "】"+  pdfMargeGetName(margeName[0]);

		PDFMergerUtility pdfMerger = new PDFMergerUtility();


		try {

			//結合させるファイルを取得
			for(int i = 0; i < paths.length; i++) {

				inputName = paths[i] + "\\" + margeName[i];

//				try(InputStream input = new FileInputStream(inputName);){
//					list.add(input);
//
//				}
				//結合させるファイルを入れる
				pdfMerger.addSource(new File(inputName));

			}

			try(FileOutputStream mergedPDFOutputStream =  new FileOutputStream(margeFile);){

				pdfMerger.setDestinationStream(mergedPDFOutputStream);
				//PDFのMerge出力
				pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());


			}
			//パスワードが空白でないとき実行
			if(!pass.equals("")) {
				pdfMargeSetPassWord(margeFile, pass);
			}

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
	 * 各フォルダから年月、コードが同じファイルを検索する
	 * @param strs　コードごとの年月、コード、名前の配列
	 * @param num　引数の順番
	 * @return true/false　
	 */
	public Boolean isYearMonthCodeMatch(String[] strs, int num) {

		ArrayList<String[]> list = null;

		// FILE_LISTの次の要素から開始
		num = num + 1;

		for(int i = num; i < FILE_LIST.size(); i++) {
			list = pdfMargeGetList(FILE_LIST.get(i));

			for(int j = 0; j < list.size(); j++) {

				//各フォルダに同じ年月、コードのファイルが存在するか検索
				if(strs[YEAR_MANTH_NUMBER].equals(list.get(j)[YEAR_MANTH_NUMBER]) && strs[EMPLOYEE_CODE_NUMBER].equals(list.get(j)[EMPLOYEE_CODE_NUMBER]) ) {

					//　各フォルダに同じ年月、コードのファイルがあるときtrueを返す
					if( i == (FILE_LIST.size() - 1)) {
						return true;
					}

					break;
				}

				// 各フォルダに同じ年月、コードのファイルがないときfalseを返す
				if(j == list.size() - 1) {
					return false;
				}

			}

		}
		return false;
	}


	/**
	 * フォルダ内のファイル名から【】を削除し、以降の文字列を"_"で3つに分割する
	 * @param strs　フォルダ内の全ファイル名
	 * @return list　年月、コード、名前
	 */
	private ArrayList<String[]> pdfMargeGetList(String[] strs) {

		String[] strlist = null ;

		ArrayList<String[]> list = new ArrayList<String[]>();

		String name = null;

		for( String s : strs) {

			//【】以降の文字列を取得
			name = pdfMargeGetName(s);

			//年月、コード、名前に分割しListに入れる
			strlist = name.split("_", 3);

			list.add(strlist);

		}

		return list;
	}


	/**
	 * 支払明細PDFファイルにパスワードを設定する
	 * @param fileName　支払明細PDFファイルパス
	 * @param pass　パスワード
	 */
	private void pdfMargeSetPassWord(String filePath, String pass) {

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
		pdfMagerFilePassCheck(file, pass);


//		//PDDocument doc = null;
//
//		PdfReader reader = null;
//		PdfStamper stamper = null;
//
//		try {
//
//			// パスワードをバイトに変更
//			byte[] ownerPassword = pass.getBytes();
//			byte[] userPassword = pass.getBytes();
//
//			byte[]bFile = Files.readAllBytes(new File(fileName).toPath());
//
//			//同じファイルを扱うためにバイト配列でPdfを開く
//			reader = new PdfReader(bFile);
//			try(FileOutputStream fileOut = new FileOutputStream(fileName)){
//				stamper = new PdfStamper(reader, fileOut);
//				// PDFファイルにパスワードを設定する
//				stamper.setEncryption(userPassword, ownerPassword, PdfWriter.ALLOW_PRINTING |PdfWriter.ALLOW_SCREENREADERS , PdfWriter.ENCRYPTION_AES_256);
//			}
//
//
//
//		} catch (IOException  e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		} finally{
//
//			if( stamper != null || reader != null){
//				try {
//					stamper.close();
//				} catch (DocumentException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}


	}

	/**
	 * 支払明細PDFファイルにパスワードが設定されているかチェック
	 * @param filePath 支払明細PDFファイルパス
	 */
	private void pdfMagerFilePassCheck(File filePath, String pass) {

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

}
