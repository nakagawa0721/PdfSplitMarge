package pdf.Split;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class PdfSplitFilePathTest {

	//引数に給与対象PDFファイルパス、賞与対象PDFファイルパス、振込対象フォルダパスを指定している場合
	@Test
	public void SplitCreate_1_1() {

		PdfSplitFilePath psfp = new PdfSplitFilePath();
		String[] paths = new String[3];
		paths[0] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\給与_TEST.pdf";
		paths[1] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\賞与_TEST.pdf";
		paths[2] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\新しいフォルダー\\202012";

		String[] expecteds = new String[3];
		expecteds[0] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\給与明細";
		expecteds[1] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\賞与明細";
		expecteds[2] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\新しいフォルダー\\202012";

		ArrayList<String> resultList = psfp.SplitCreate(paths);

		assertEquals(resultList.get(0), expecteds[0]);
		assertEquals(resultList.get(1), expecteds[1]);
		assertEquals(resultList.get(2), expecteds[2]);


	}



	//引数に給与対象PDFファイルパス、振込対象フォルダパスを指定している場合
	@Test
	public void SplitCreate_1_2() {

		PdfSplitFilePath psfp = new PdfSplitFilePath();
		String[] paths = new String[2];
		paths[0] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\給与_TEST.pdf";
		paths[1] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\新しいフォルダー\\202012";

		String[] expecteds = new String[2];
		expecteds[0] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\給与明細";
		expecteds[1] = "C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\新しいフォルダー\\202012";

		ArrayList<String> resultList = psfp.SplitCreate(paths);

		assertEquals(resultList.get(0), expecteds[0]);
		assertEquals(resultList.get(1), expecteds[1]);

	}


	//引数に給与明細PDFファイルを指定している場合
	@Test
	public void getDetailsTest_1_1() throws Exception {

		PdfSplitFilePath psfp = new PdfSplitFilePath();
		String[] paths = new String[]{"C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\給与_TEST.pdf"};

		String expected = "給与明細";

		psfp.SplitCreate(paths);



		Method method = PdfSplitFilePath.class.getDeclaredMethod("getDetails");

		method.setAccessible(true);
		String result = (String)method.invoke(psfp);

		assertEquals(result, expected);


	}

	//引数に賞与明細PDFファイルを指定している場合
	@Test
	public void getDetailsTest_1_2() throws Exception {

		PdfSplitFilePath psfp = new PdfSplitFilePath();
		String[] paths = new String[]{"C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\賞与_TEST.pdf"};

		String expected = "賞与明細";

		psfp.SplitCreate(paths);

		Method method = PdfSplitFilePath.class.getDeclaredMethod("getDetails");

		method.setAccessible(true);
		String result = (String)method.invoke(psfp);

		assertEquals(result, expected);


	}

	//引数に給与、賞与明細以外を指定している場合
	@Test
	public void getDetailsTest_1_3() throws Exception {

		PdfSplitFilePath psfp = new PdfSplitFilePath();
		String[] paths = new String[]{"C:\\Users\\Heisei\\Documents\\nakagawa\\PDF\\pdftest.pdf"};

		String expected = null;

		psfp.SplitCreate(paths);

		Method method = PdfSplitFilePath.class.getDeclaredMethod("getDetails");

		method.setAccessible(true);
		String result = (String)method.invoke(psfp);

		assertEquals(result, expected);


	}

	//引数が給与支払明細書の場合、給与が戻るか
	@Test
	public void IsDetails_1_1() throws Exception {

		PdfSplitFilePath psfp = new PdfSplitFilePath();

		String expected = "給与明細";

		Method method = PdfSplitFilePath.class.getDeclaredMethod("IsDetails",String.class);

		method.setAccessible(true);
		String result = (String)method.invoke(psfp, "給与支払明細書");

		assertEquals(result, expected);


	}

	//引数が賞与支給明細書の場合、賞与が戻るか
	@Test
	public void IsDetails_1_2() throws Exception {

		PdfSplitFilePath psfp = new PdfSplitFilePath();

		String expected = "賞与明細";

		Method method = PdfSplitFilePath.class.getDeclaredMethod("IsDetails",String.class);

		method.setAccessible(true);
		String result = (String)method.invoke(psfp, "賞与支給明細書");

		assertEquals(result, expected);


	}

	//引数が給与支払明細書,賞与支給明細書以外の場合、nullが戻るか
	@Test
	public void IsDetails_1_3() throws Exception {

		PdfSplitFilePath psfp = new PdfSplitFilePath();

		String expected = null;

		Method method = PdfSplitFilePath.class.getDeclaredMethod("IsDetails",String.class);

		method.setAccessible(true);
		String result = (String)method.invoke(psfp, "振込明細書");

		assertEquals(result, expected);


	}

	//引数がpassword=heiseiの場合
	@Test
	public void isPassWordCheckTest_1_1() {

		PdfSplitFilePath psfp = new PdfSplitFilePath();
	}


}
