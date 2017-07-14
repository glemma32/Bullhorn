package byaj.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity 
public class Like {
		
		@Id
		private Long id; 
		
		@ManyToOne
		private User Liked;  
		
		private String Status; 
		
		private Long photoid;
		
		@CreationTimestamp 
		Date likedDate;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getLiked() {
			return Liked;
		}

		public void setLiked(User liked) {
			Liked = liked;
		}

		public String getStatus() {
			return Status;
		}

		public void setStatus(String status) {
			Status = status;
		}

		public Date getlikedDate() {
			return likedDate;
		}

		public void setlikedDate(Date likedDate) {
			this.likedDate = likedDate;
		}

		public Long getPhotoid() {
			return photoid;
		}

		public void setPhotoid(Long photoid) {
			this.photoid = photoid;
		}

		
}
