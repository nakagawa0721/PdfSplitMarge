package pdf.Split;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfSplitFilePath {

//	private String[] pdfPage ;

	//明細書の桁数
	private static final int DETAILS_NUMBER = 7;

	//給与賞与検索
	private static final String SALARY_BONUS_MATCHES = "明細書";
	//給与
	private static final String SALARY_MATCHE = "給与明細";
	//賞与
	private static final String BONUS_MATCHE = "賞与明細";

	public ArrayList<String> SplitCreate(String[] pdfPaths) {

		String targetPath = null;

		String details = null;

		String fileName = null;

		String pdfName = null;

		String savePath = null;

		SalarySplit ssp = new SalarySplit();

		BonusSplit bsp = new BonusSplit();

		String[] pdfPage = null;


		ArrayList<String> pdfList = new ArrayList<String>();

		for( String pdf : pdfPaths) {

			targetPath = FilenameUtils.getFullPath(pdf);


			File pdfFile = new File(pdf);



			//引数がファイルの時
			if(pdfFile.isFile()) {

				try(PDDocument pdDoc = PDDocument.load(pdfFile);) {
					int numberOfPages = pdDoc.getNumberOfPages();

					for(int n = 0; n < numberOfPages; n++) {


						try (PDDocument pdc = new PDDocument();){
							// ドキュメントオブジェクトの作成

							//ページオブジェクトの作成
							PDPage page = (PDPage) pdDoc.getPage( n );
							pdc.addPage(page);

							//PDFf内のテキストを取得
							PDFTextStripper stripper = new PDFTextStripper();
							String text = stripper.getText(pdc);
							String strtext = text;

							//改行ごとに配列に追加
							pdfPage = strtext.split("\n");

							//明細名を取得する
							details = getDetails(pdfPage);

							if("給与明細".equals(details)) {
								//給与明細分割処理
								fileName = ssp.salarySplitPdf(pdfPage);
							}else if("賞与明細".equals(details)) {
								//賞与明細分割処理
								fileName = bsp.bonusSplitPdf(pdfPage);
							}else {
								System.out.println("引数が給与明細または賞与明細のPDFファイルではありません。引数を変更してください。");
								System.out.println("処理を終了します。");
								System.exit(0);
							}


							//保存先フォルダが存在しない場合、新規作成
							savePath = targetPath +  details;
							File newdir = new File(savePath);
							if(!newdir.exists()) {
								newdir.mkdir();
							}

							// ファイル名を指定
							pdfName = savePath + "\\" + "【" + details + "】" + fileName;

							// 保存
							pdc.save(pdfName);

						}
					}
					System.out.println(details + "の分割成功しました。");

				}catch (IOException e) {
					//e.printStackTrace();
					System.out.println(pdfFile + "はPDFファイルではありません。");
					System.out.println("処理を終了します。");
					System.exit(0);
				}

				//引数がフォルダの時
			}else if(pdfFile.isDirectory()){
				//振込明細
				//tdc.TransferDirCreate(pdf,targetPath);
				savePath = pdf;

				//引数が文字列の時
			}else {
//				isPassWordCheck(pdf);
				continue;
			}

			pdfList.add(savePath);



		}

		//System.out.println("Split PDF File Complete!!");

		return pdfList;

	}

	/**
	 * 給与明細か賞与明細かを判別する
	 *
	 * @return str
	 */
	private  String getDetails(String[] pdfPage) {

		String str = null;

		String detailsName = null;

		for(String pdf : pdfPage) {
			if(pdf.trim().length() >= DETAILS_NUMBER) {
				str = pdf.trim();

				detailsName = IsDetails(str);
				if(detailsName != null) {
					return detailsName;
				}
			}
		}
		return null;

		//		for(int i = 0; i < pdfPage.length; i++) {
		//			if(pdfPage[i].trim().length() >= DETAILS_NUMBER) {
		//				str = pdfPage[i].trim();
		//
		////				if(IsDetails(str).equals(SALARY_MATCHE) || IsDetails(str).equals(BONUS_MATCHE) ) {
		////					return IsDetails(str) + "明細" ;
		////				}
		//
		//				detailsName = IsDetails(str);
		//
		//				if(detailsName != null) {
		//					return detailsName;
		//				}
		//
		//			}
		//		}

	}


	/**
	 * 明細書の文字列を判別する
	 * @param str
	 * @return
	 */
	private String IsDetails(String str) {

		String detailsName = null;

		//明細書と書かれて文字列を検索
		if(str.contains(SALARY_BONUS_MATCHES)) {
			//最初の2文字を戻す
			detailsName = str.substring(0,2) + "明細";
		}

		if(SALARY_MATCHE.equals(detailsName) || BONUS_MATCHE.equals(detailsName)) {
			return detailsName;
		}

		return null;
	}




}
