# Job-Tracker

Job-Tracker is a full-stack application designed to streamline job application management. It allows users to store details about their job applications, track progress, and collaborate effectively. The project covers both the backend (Java, Spring Boot, MySQL) and frontend (HTML, CSS, JavaScript, React.js).

## Features

1. **Application Details:**
   - Maintain a database of job applications, including company names, positions, and application dates.
   - Create, update, and delete application records.

2. **Role-Based Access:**
   - Implemented **Spring Security** for secure authentication and authorization.
   - Multiple users can create accounts and manage their job application information.

3. **Collaboration and Efficiency:**
   - Collaborate with other users by sharing insights and tips.
   - Leverage various features to enhance job search efficiency.

4. **Daily Tracking and Insights:**
   - APIs for tracking daily job applications.
   - Visualize application trends using charts.

5. **Comparison and Motivation:**
   - Compare your application frequency with the top 3 users.
   - Stay motivated to maintain a consistent job search routine.

## Getting Started

1. **Prerequisites:**
   - Java Development Kit (JDK) 17+
   - Maven 3+
   - MySQL database (create a schema named `job_tracker`)

2. **Clone the Repository:**
   ```bash
   git clone https://github.com/yourusername/job-tracker.git
   cd job-tracker
   ```

3. **Database Configuration:**
   - Update `application.properties` with your MySQL credentials.

4. **Build and Run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application:**
   - Open [http://localhost:8080](http://localhost:8080) in your browser.

## Deployment (Previously Hosted on AWS)

The application was previously hosted on AWS using S3 buckets and Elastic Beanstalk. Due to the free tier limitations, the instance has been removed for now.

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.