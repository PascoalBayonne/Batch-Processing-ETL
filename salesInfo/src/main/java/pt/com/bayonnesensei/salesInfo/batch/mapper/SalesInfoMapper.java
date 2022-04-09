package pt.com.bayonnesensei.salesInfo.batch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pt.com.bayonnesensei.salesInfo.batch.dto.SalesInfoDTO;
import pt.com.bayonnesensei.salesInfo.domain.SalesInfo;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SalesInfoMapper {

    SalesInfo mapToEntity(SalesInfoDTO salesInfoDTO);
}
