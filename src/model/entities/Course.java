package model.entities;

public class Course {

		private String code, name, description, status;
		private int max_capacity;
		
		
		
		
		/**
		 * @param code
		 * @param name
		 * @param description
		 * @param status
		 * @param max_capacity
		 */
		public Course(String code, String name, String description, int max_capacity, String status) {
			super();
			this.code = code;
			this.name = name;
			this.description = description;
			this.status = status;
			this.max_capacity = max_capacity;
		}
		
		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}
		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		/**
		 * @return the max_capacity
		 */
		public int getMax_capacity() {
			return max_capacity;
		}
		/**
		 * @param max_capacity the max_capacity to set
		 */
		public void setMax_capacity(int max_capacity) {
			this.max_capacity = max_capacity;
		}
		
		@Override
		public String toString() {
			return "Course [code=" + code + ", name=" + name + ", description=" + description + ", status=" + status
					+ ", max_capacity=" + max_capacity + "]";
		}
		
		
		
		
}
