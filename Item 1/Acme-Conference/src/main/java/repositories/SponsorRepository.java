
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	@Query("select s from Sponsor s where s.userAccount.id=?1")
	Sponsor findByUserId(final Integer id);

	@Query("select s.email from Sponsor s where s.email =?1")
	String checkForEmailInUse(String email);

}
