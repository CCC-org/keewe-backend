package ccc.keewedomain.persistence.repository.report;

import ccc.keewedomain.persistence.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
