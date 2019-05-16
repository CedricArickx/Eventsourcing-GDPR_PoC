package com.xti.eventsourcingbackend.repository;

import com.xti.eventsourcingbackend.domain.EncryptionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncryptionKeyRepository extends JpaRepository<EncryptionKey, Long> {
}
