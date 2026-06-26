package com.lluanps.raizes_nordeste_api_uninter.logs.repository;

import com.lluanps.raizes_nordeste_api_uninter.logs.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Integer> {

}
