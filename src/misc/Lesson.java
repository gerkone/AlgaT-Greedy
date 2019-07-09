package misc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Lesson {
		private String title;
		private String descrizione;
		private String resource;
		private Double width;
		private Double height;
		public Lesson(String nome, String descrizione, String resource, Double width, Double height) {
			super();
			this.title = nome;
			this.descrizione = descrizione;
			this.width = width;
			this.height = height;
			try {
				this.resource = resource;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public Double getWidth() {
			return width;
		}
		public Double getHeight() {
			return height;
		}
		public String getTitle() {
			return title;
		}
		public String getDescription() {
			return descrizione;
		}
		public String getResource() {
			return resource;
		}
		
	}