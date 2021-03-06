
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Folder f join f.messages m where m.sender.id=?1 and f.actor.id=?1")
	Collection<Message> findAllBySender(int senderId);

	@Query("select m from Folder f join f.messages m join m.recivers r where (r.id=?2 or m.sender.id=?2) and m.topic.id=?1 and f.actor.id=?2")
	Collection<Message> findAllByTopic(int topicId, int actorId);

	@Query("select m from Folder f join f.messages m join m.recivers r where (r.id=?1 or m.sender.id=?1) and f.actor.id=?1")
	Collection<Message> findAllByPrincipal(int actorId);

	@Query("select case when count(m)>0 then true else false end from Folder f join f.messages m join m.recivers r where (r.id=?2 or m.sender.id=?2) and m.id=?1 and f.actor.id=?2")
	Boolean findByPrincipal(int messageId, int actorId);

	//TODO ALBA
	@Query("select m from Folder f join f.messages m join m.recivers r where r.id=?1 and f.actor.id=?1")
	Collection<Message> findAllByRecipient(int recipientId);

	@Query("select m from Folder f join f.messages m where f.id=?1 and f.actor.userAccount.id=?2")
	Collection<Message> findAllByFolderIdAndUserId(Integer mid, Integer uid);

	@Query("select m from Folder f join f.messages m join m.recivers r where r.id=?2 and f.actor.id=?1")
	Collection<Message> findAllByRecipient(int principalId, int actorId);

}
