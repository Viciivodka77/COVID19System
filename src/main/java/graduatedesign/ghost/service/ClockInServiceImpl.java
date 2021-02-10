package graduatedesign.ghost.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.mapper.ClockInMapper;
import graduatedesign.ghost.pojo.ClockIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
public class ClockInServiceImpl implements ClockInService {

    private ClockInMapper clockInMapper;
    private DataSourceTransactionManager transactionManager;

    @Autowired
    public ClockInServiceImpl(ClockInMapper clockInMapper,
                              DataSourceTransactionManager transactionManager){
        this.clockInMapper = clockInMapper;
        this.transactionManager = transactionManager;
    }


    @Override
    public PageInfo<ClockIn> queryAllClockIn(int page,int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<ClockIn> clockInList = clockInMapper.queryAllClockIn();
        return new PageInfo<>(clockInList,5);
    }

    @Override
    public PageInfo<ClockIn> querySomeoneClockIn(int uID, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<ClockIn> clockInList = clockInMapper.querySomeoneClockIn(uID);
        return new PageInfo<>(clockInList,5);
    }

    @Override
    public ClockIn querySomeoneNewOneClockIn(int uID) {
        return clockInMapper.querySomeoneNewOneClockIn(uID);
    }

    @Override
    public int addClockIn(ClockIn clockIn) {
        //配置事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("clockIn-transaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置状态点
        TransactionStatus status = transactionManager.getTransaction(def);
        int i = 0;
        try {
            //打卡
            i = clockInMapper.addClockIn(clockIn);
            //根据用户身心健康判断是否需要下派管理员联系用户
            //...
            //手动提交事务
            transactionManager.commit(status);
        }catch (Exception e){
            //异常回滚
            e.printStackTrace();
            transactionManager.rollback(status);
        }
        return i;
    }
}
