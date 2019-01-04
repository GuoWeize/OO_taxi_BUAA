package homework11;

public interface Car {
	public void apply ();
	public void wander ();
	public void serve ();
	public void rest ();
	public Point getlocation ();
	public Carstate getstate ();
}


enum Carstate {
	stopping,
	waiting,
	ordering,
	serving,
	
}