package one.backbone.messagingassignment.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import one.backbone.messagingassignment.model.dto.MessageFilterDto;
import one.backbone.messagingassignment.model.entity.Message;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MessageSpecification implements Specification<Message> {

    private final MessageFilterDto filter;

    public MessageSpecification(MessageFilterDto filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("deleted"), filter.isDeleted()));

        if (filter.getContent() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + filter.getContent().toLowerCase() + "%"));
        }

        if (filter.getSenderId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("sender").get("id"), filter.getSenderId()));
        }

        if (filter.getRecipientId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("recipient").get("id"), filter.getRecipientId()));
        }

        if (filter.getCreatedAt() != null) {
            predicates.add(criteriaBuilder.equal(root.get("createdAt"), filter.getCreatedAt()));
        } else {
            if (filter.getDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filter.getDateFrom()));
            }

            if (filter.getDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filter.getDateTo()));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}