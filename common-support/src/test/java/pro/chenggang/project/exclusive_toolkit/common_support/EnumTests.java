package pro.chenggang.project.exclusive_toolkit.common_support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hamcrest.Matchers;
import org.junit.Test;
import pro.chenggang.project.exclusive_toolkit.common_support.general.EnumInstanceFinder;
import pro.chenggang.project.exclusive_toolkit.common_support.general.EnumSupport;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author: chenggang
 * @date 2020-10-21.
 */
public class EnumTests {

    @Getter
    @AllArgsConstructor
    enum DemoEnum {

        T1("FINISHED",1),
        T2("STARTING",2),

        ;

        @EnumSupport
        private String displayName;
        @EnumSupport
        private Integer status;

    }

    @Test
    public void test(){
        Optional<DemoEnum> optionalDemoEnum= EnumInstanceFinder.getEnum(DemoEnum.class, "status", 1);
        assertThat(optionalDemoEnum.isPresent(), Matchers.is(true));
    }

    @Test
    public void threadTest() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        IntStream.range(1, 4)
                .forEach(index -> {
                    executorService.execute(new Thread(() -> {
                        List<DemoEnum> allEnums = EnumInstanceFinder.getAllEnums(DemoEnum.class, "name", "t"+index);
                        System.out.println("Thread:"+Thread.currentThread().getName()+" --> " + allEnums);
                        countDownLatch.countDown();
                    }));
                });
        countDownLatch.await();
    }
}
