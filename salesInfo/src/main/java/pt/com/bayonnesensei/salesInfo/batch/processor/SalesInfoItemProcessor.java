package pt.com.bayonnesensei.salesInfo.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import pt.com.bayonnesensei.salesInfo.batch.dto.SalesInfoDTO;
import pt.com.bayonnesensei.salesInfo.batch.mapper.SalesInfoMapper;
import pt.com.bayonnesensei.salesInfo.domain.SalesInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesInfoItemProcessor implements ItemProcessor<SalesInfoDTO, SalesInfo> {
    private final SalesInfoMapper salesInfoMapper;
    AtomicInteger count = new AtomicInteger(0);

    @Override
    public SalesInfo process(SalesInfoDTO item) throws Exception {
        Thread.sleep(200);// maybe hitting an external api
        log.info("processing the item: {}", item.toString());
        int i = count.addAndGet(1);
        if (i == 2) {
            throw new IllegalArgumentException();
        }
        return salesInfoMapper.mapToEntity(item);
    }
}
