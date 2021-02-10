package graduatedesign.ghost.service;

import graduatedesign.ghost.mapper.WebInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebInfoServiceImpl implements WebInfoService {

    private WebInfoMapper webInfoMapper;

    @Autowired
    public WebInfoServiceImpl(WebInfoMapper webInfoMapper) {
        this.webInfoMapper = webInfoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateNotice(String notice, int adminID) {
        return webInfoMapper.updateNotice(notice,adminID);
    }
}
