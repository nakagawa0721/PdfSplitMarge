package PdfMarge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;


public class pdfMargeProcess {

	//給与、賞与、振込明細のフォルダ内のファイルを追加するList
	ArrayList<String[]> FILE_LIST = new ArrayList<String[]>();

	//ArrayList<String[]> list = new ArrayList<String[]>();

	//年月の要素数
	private int YEAR_MANTH_NUMBER = 0;

	//コードの要素数
	private int EMPLOYEE_CODE_NUMBER = 1;

	private String PAYMENT_DETAILS_PATH = "\\【支払明細】";

	private String PAYMENT_DETAILS_FOLDER_PATH = "\\支払明細";


	/**
	 * ファイル名から【】を削除
	 * @param fileName pdfファイル名
	 * @return name 【】削除したファイル名
	 */
	public String pdfMargeGetName(String fileName) {

		String name = null;

		String p = null;

		for(int i = 0; i < fileName.length(); i++) {
			p = String.valueOf(fileName.charAt(i));
			//文字列から "】"を検索し、それ以降の文字列を取得
			if(p.equals("】")) {
				name = fileName.substring(i + 1);
				break;
			}
		}
		return name ;
	}


	/**
	 *	各フォルダに入ってあるpdfファイル名をListに全て入れる
	 * @param paths　各フォルダパス
	 */
	public void pdfFileNameSetList(String[] paths) {


		for(int i = 0; i < paths.length ; i++) {
			File f = new File(paths[i]);
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
					pdfMargeFileCreate(paths,margeName ,pass);

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
		List<InputStream> list = new ArrayList<InputStream>();
		String margeFile = null;

		// 支払明細のファイルパスを取得
		margeFile = getSaveFolder(paths[0]) +  PAYMENT_DETAILS_PATH +  pdfMargeGetName(margeName[0]);


		try {

			//結合させるファイルを取得
			for(int i = 0; i < paths.length; i++) {

				inputName = paths[i] + "\\" + margeName[i];

				InputStream input = new FileInputStream(inputName);

				list.add(input);

			}

			FileOutputStream mergedPDFOutputStream =  new FileOutputStream(margeFile);

			PDFMergerUtility pdfMerger = new PDFMergerUtility();
			pdfMerger.addSources(list);
			pdfMerger.setDestinationStream(mergedPDFOutputStream);

			//PDFのMerge出力
			pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

			//パスワードが空白でないとき実行
			if(!pass.equals("")) {
				pdfMargeSetPassWord(margeFile, pass);
			}



		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}


	/**
	 * 支払明細のファイルパスを取得する
	 * @param path　第一引数のフォルダパス
	 * @return filePath 支払明細のフォルダパス
	 */
	public String getSaveFolder(String path) {

		String p = null;
		String folderPath = null;

		StringBuilder strb = new StringBuilder(path);
		for(int j = strb.length() -1 ; j > 0 ; j--) {
			p = String.valueOf(strb.charAt(j));
			//フォルダ名を削除
			if(p.equals("\\")) {
				strb.setLength(path.length() - (path.length() - j));

				folderPath = strb.toString() + PAYMENT_DETAILS_FOLDER_PATH;

				return folderPath;

			}
		}

		return folderPath;

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

		String str = null;

		String p = null;

		String[] strlist = null ;

		ArrayList<String[]> list = new ArrayList<String[]>();

		for(int i = 0; i < strs.length; i++) {

			for(int j = 0; j < strs[i].length(); j++) {
				p = String.valueOf(strs[i].charAt(j));
				//文字列から "】"を検索し、それ以降の文字列を取得
				if(p.equals("】")) {
					str = strs[i].substring(j + 1);

					//年月、コード、名前に分割しListに入れる
					strlist = str.split("_", 3);

					list.add(strlist);

					break;

				}

			}

		}

		return list;
	}


	/**
	 * 支払明細PDFファイルにパスワードを設定する
	 * @param fileName　支払明細PDFファイルパス
	 * @param pass　パスワード
	 */
	private void pdfMargeSetPassWord(String fileName, String pass) {

		//PDDocument doc = null;

		PdfReader reader = null;
		PdfStamper stamper = null;

		try {

			// パスワードをバイトに変更
			byte[] ownerPassword = pass.getBytes();
			byte[] userPassword = pass.getBytes();

			byte[]bFile = Files.readAllBytes(new File(fileName).toPath());

			//同じファイルを扱うためにバイト配列でPdfを開く
			reader = new PdfReader(bFile);
			stamper = new PdfStamper(reader, new FileOutputStream(fileName));

			// PDFファイルにパスワードを設定する
			stamper.setEncryption(userPassword, ownerPassword, PdfWriter.ALLOW_PRINTING |PdfWriter.ALLOW_SCREENREADERS , PdfWriter.ENCRYPTION_AES_256);



		} catch (IOException  e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally{

			if( stamper != null || reader != null){
				try {
					stamper.close();
					reader.close();
				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//パスワードが設定されているか確認する
		pdfMagerFilePassCheck(fileName);


	}

	/**
	 * 支払明細PDFファイルにパスワードが設定されているかチェック
	 * @param filePath 支払明細PDFファイルパス
	 */
	private void pdfMagerFilePassCheck(String filePath) {

		boolean isValidPdf = true;

		try {
			new PdfReader(filePath);
			isValidPdf = false;
		}catch (BadPasswordException e) {
			//パスワードが設定されている場合はisValidPdf=true

		} catch (Exception e) {
			e.printStackTrace();
			isValidPdf = false;
		}

		if(!isValidPdf) {
			System.out.println(filePath + "の暗号化に失敗しました。");
		}

	}

}
