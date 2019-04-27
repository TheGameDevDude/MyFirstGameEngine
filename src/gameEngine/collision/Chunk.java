package gameEngine.collision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Chunk implements Serializable {
	public List<AABB> aabbs = new ArrayList<AABB>();
	public int chunkSize;

	public Chunk(int chunkSize, List<AABB> otherAabbs) {
		this.chunkSize = chunkSize;
		for (AABB aabb : otherAabbs) {
			aabbs.add(aabb);
		}
	}
}
