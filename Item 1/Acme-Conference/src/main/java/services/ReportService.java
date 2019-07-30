
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ReportRepository;
import domain.Report;
import domain.Reviewer;
import domain.Submission;

@Service
@Transactional
public class ReportService {

	@Autowired
	private ReportRepository	reportRepository;


	public Report create() {
		final Report res = new Report();
		return res;
	}

	public Report save(final Report report, final Submission submission, final Reviewer reviewer) {
		Assert.notNull(report);
		Assert.notNull(submission);
		Assert.notNull(reviewer);
		Assert.isTrue(report.getDecision().matches("^(BORDER-LINE|ACCEPT|REJECT)$"));
		Assert.notNull(report.getReadability());
		Assert.isTrue(report.getReadability() >= 0 && report.getReadability() <= 10);
		Assert.notNull(report.getQuality());
		Assert.isTrue(report.getQuality() >= 0 && report.getQuality() <= 10);
		Assert.notNull(report.getOriginality());
		Assert.isTrue(report.getOriginality() >= 0 && report.getOriginality() <= 10);

		report.setSubmission(submission);
		report.setReviewer(reviewer);

		final Report res = this.reportRepository.save(report);
		Assert.notNull(res);

		return res;
	}

	public Report findOne(final Integer reportId) {
		Assert.notNull(reportId);
		final Report res = this.reportRepository.findOne(reportId);
		Assert.notNull(reportId);

		return res;
	}

	public Collection<Report> findAll() {
		final Collection<Report> res = this.reportRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public void delete(final Integer reportId) {
		Assert.notNull(reportId);
		this.reportRepository.delete(reportId);
	}
}
