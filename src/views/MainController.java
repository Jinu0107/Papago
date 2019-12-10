package views;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainController {
	@FXML
	private TextField field;

	@FXML
	private Label la;

	@FXML
	private Label gam;
	@FXML
	private Label h;

	private String word;

	private String text1;

	private String langcode;

	public void ko() {
		word = "ko";
		h.setText("한국어");
	}

	public void en() {
		word = "en";
		h.setText("영어");
	}

	public void ja() {
		word = "ja";
		h.setText("일본어");
	}

	public void zhCN() {
		word = "zh-CN";
		h.setText("중국간체");
	}

	public void zhTW() {
		word = "zh-TW";
		h.setText("중국번체");
	}

	public void start() {
		text1 = field.getText();
		sensing();
		if (langcode.equals("ko")) {
			gam.setText("한국어");
			if(word.equals("ko")) {
				en();
			}
		} else if (langcode.equals("ja")) {
			gam.setText("일본어");
		} else if (langcode.equals("zh-cn")) {
			gam.setText("중국어 간체");
		} else if (langcode.equals("zh-tw")) {
			gam.setText("중국어 번체");
		} else if (langcode.equals("hi")) {
			gam.setText("힌디어");
		} else if (langcode.equals("en")) {
			gam.setText("영어");
			if(word.equals("en")) {
				ko();
			}
		} else if (langcode.equals("es")) {
			gam.setText("스페인어");
		} else if (langcode.equals("fr")) {
			gam.setText("프랑스어");
		} else if (langcode.equals("de")) {
			gam.setText("독일어");
		} else if (langcode.equals("pt")) {
			gam.setText("포루투갈어");
		} else if (langcode.equals("vi")) {
			gam.setText("베트남어");
		} else {
			gam.setText("귀찮아서 감지 불가");
		}
		
		translation();
	}

	public void key(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			start();
		}

	}
	
	@FXML
	private void initialize() {
		word = "ko";
	}
	public void exit() {
		Platform.exit();
	}

	public void sensing() {
		String clientId = "zNhRJ9ecTPh7Ro763Xor";// 애플리케이션 클라이언트 아이디값";
		String clientSecret = "KwptPP5oIy";// 애플리케이션 클라이언트 시크릿값";
		try {
			String query = URLEncoder.encode(text1, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			// post request
			String postParams = "query=" + query;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			String json = response.toString();

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(json);
			langcode = element.getAsJsonObject().get("langCode").getAsString();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void translation() {

		String clientId = "zNhRJ9ecTPh7Ro763Xor";// 애플리케이션 클라이언트 아이디값";
		String clientSecret = "KwptPP5oIy"; // 애플리케이션 클라이언트 시크릿값";
		String apiURL = "https://openapi.naver.com/v1/language/translate";
		try {
			String text = URLEncoder.encode(text1, "UTF-8");
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			// post request
			String postParams = "source=" + langcode + "&target=" + word + "&text=" + text;

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			String json = response.toString();
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(json);
			String temp = element.toString();
			if (!temp.substring(2, 14).equals("errorMessage")) {
				String abc = element.getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject()
						.get("translatedText").getAsString();
				la.setText(abc);
			} else {
				la.setText("번역할 수 없습니다. 다시 입력하세요");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
