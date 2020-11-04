package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.TransactionDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.Transaction;
import de.ocplearn.hv.model.Unit;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

	TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
	
	TransactionDto TransactionToTransactionDto(Transaction transaction);
	
	Transaction TransactionDtoToTransaction(TransactionDto transactionDto);
}
