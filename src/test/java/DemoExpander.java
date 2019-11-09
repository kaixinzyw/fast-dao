//import com.fast.aspect.ExpanderOccasion;
//import com.fast.aspect.FastDaoExpander;
//import com.fast.fast.FastDaoParam;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DemoExpander implements FastDaoExpander {
//
//    /**
//     * @param param 封装了DAO所有的执行参数
//     * @return 是否执行
//     */
//    @Override
//    public boolean before(FastDaoParam param) {
//        System.out.println("DAO执行前");
//        return true;
//    }
//
//    /**
//     * @param param 封装了DAO所有的执行参数
//     */
//    @Override
//    public void after(FastDaoParam param) {
//        System.out.println("DAO执行后");
//    }
//
//    @Override
//    public List<ExpanderOccasion> occasion() {
//        //配置DAO切面执行时机
//        List<ExpanderOccasion> list = new ArrayList<>();
//        list.add(ExpanderOccasion.SELECT);
//        list.add(ExpanderOccasion.UPDATE);
//        return list;
//    }
//
//}
