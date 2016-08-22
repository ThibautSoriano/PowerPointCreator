package slideshow;

import java.util.ArrayList;
import java.util.List;

public class Slideshow {
	private List<RawSlide> slides;
	
	public Slideshow() {
		this.slides = new ArrayList<>();
	}
	
	public Slideshow(ArrayList<RawSlide> slides) {
		this.slides = slides;
	}
	
	public void addSlide(RawSlide slide){
		this.slides.add(slide);
	}
	
	public RawSlide getSlideByNumber(int number){
		
		for (RawSlide rawSlide : slides) {
			if(rawSlide.getNumber() == number)
				return rawSlide;
		}
		
		return null;
	}
}
