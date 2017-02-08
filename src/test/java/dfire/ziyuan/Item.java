package dfire.ziyuan;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {

    private static final long serialVersionUID = -8802530334638107787L;
    /**
     */
    private List<Item> childItems;
    /**
     */
    private Short kind = 0;
    /**
     */
    private String parentId = "";
    /**
     */
    private Short priceMode = 0;
    /**
     */
    private String kindMenuId = "";
    /**
     */
    private String menuId = "";
    /**
     */
    private String name = "";
    /**
     */
    private String makeId = "";
    /**
     */
    private String makeName = "";
    /**
     */
    private Double makePrice = 0.0;
    /**
     */
    private Short makePriceMode = 0;
    /**
     */
    private String specDetailId = "";
    /**
     */
    private String specDetailName = "";
    /**
     */
    private Short specPriceMode = 0;
    /**
     */
    private Double specDetailPrice = 0.0;
    /**
     */
    private Double num = 0.0;
    /**
     */
    private Double accountNum = 0.0;
    /**
     */
    private String unit = "";
    /**
     */
    private String accountUnit = "";
    /**
     */
    private String memo = "";
    /**
     */
    private Double originalPrice = 0.0;
    /**
     */
    private Double price = 0.0;
    /**
     */
    private Double fee = 0.0;
    /**
     */
    private Double ratioFee = 0.0;
    /**
     */
    private Short isRatio = 0;
    /**
     */
    private Double ratio = 0.0;
    /**
     */
    private Double memberPrice = 0.0;
    /**
     */
    private Short status = -1;
    /**
     */
    private String entityId = "";
    /**
     */
    private String index = "";

    /**
     */
    private int startNum;

    /**
     */
    private String imagePath = "";
    /**
     */
    private String kindMenuName = "";

    /**
     */
    private int addPriceMode;
    /**
     */
    private double addPrice;

    /**
     */
    private String suitMenuDetailId = "";

    /**
     */
    private Boolean isCompulsory = false;

    /**
     */
    private int isRequired;

    /**
     */
    private Boolean isChildCause;

    private int stepLength = 1;

    private Short isWait = 0;

    private int tag = 0;

    private String id = "";

    /**
     * 商品类型
     */
    private int type = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<Item> childItems) {
        this.childItems = childItems;
    }

    public Short getKind() {
        return kind;
    }

    public void setKind(Short kind) {
        this.kind = kind;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Short getPriceMode() {
        return priceMode;
    }

    public void setPriceMode(Short priceMode) {
        this.priceMode = priceMode;
    }

    public String getKindMenuId() {
        return kindMenuId;
    }

    public void setKindMenuId(String kindMenuId) {
        this.kindMenuId = kindMenuId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMakeId() {
        return makeId;
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public Double getMakePrice() {
        return makePrice;
    }

    public void setMakePrice(Double makePrice) {
        this.makePrice = makePrice;
    }

    public Short getMakePriceMode() {
        return makePriceMode;
    }

    public void setMakePriceMode(Short makePriceMode) {
        this.makePriceMode = makePriceMode;
    }

    public String getSpecDetailId() {
        return specDetailId;
    }

    public void setSpecDetailId(String specDetailId) {
        this.specDetailId = specDetailId;
    }

    public String getSpecDetailName() {
        return specDetailName;
    }

    public void setSpecDetailName(String specDetailName) {
        this.specDetailName = specDetailName;
    }

    public Short getSpecPriceMode() {
        return specPriceMode;
    }

    public void setSpecPriceMode(Short specPriceMode) {
        this.specPriceMode = specPriceMode;
    }

    public Double getSpecDetailPrice() {
        return specDetailPrice;
    }

    public void setSpecDetailPrice(Double specDetailPrice) {
        this.specDetailPrice = specDetailPrice;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Double getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(Double accountNum) {
        this.accountNum = accountNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAccountUnit() {
        return accountUnit;
    }

    public void setAccountUnit(String accountUnit) {
        this.accountUnit = accountUnit;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getRatioFee() {
        return ratioFee;
    }

    public void setRatioFee(Double ratioFee) {
        this.ratioFee = ratioFee;
    }

    public Short getIsRatio() {
        return isRatio;
    }

    public void setIsRatio(Short isRatio) {
        this.isRatio = isRatio;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getKindMenuName() {
        return kindMenuName;
    }

    public void setKindMenuName(String kindMenuName) {
        this.kindMenuName = kindMenuName;
    }

    public int getAddPriceMode() {
        return addPriceMode;
    }

    public void setAddPriceMode(int addPriceMode) {
        this.addPriceMode = addPriceMode;
    }

    public double getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(double addPrice) {
        this.addPrice = addPrice;
    }

    public String getSuitMenuDetailId() {
        return suitMenuDetailId;
    }

    public void setSuitMenuDetailId(String suitMenuDetailId) {
        this.suitMenuDetailId = suitMenuDetailId;
    }

    public Boolean getIsCompulsory() {
        return isCompulsory;
    }

    public void setIsCompulsory(Boolean isCompulsory) {
        this.isCompulsory = isCompulsory;
    }


    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getIsChildCause() {
        return isChildCause;
    }

    public void setIsChildCause(Boolean isChildCause) {
        this.isChildCause = isChildCause;
    }

    public int getStepLength() {
        return stepLength;
    }

    public void setStepLength(int stepLength) {
        this.stepLength = stepLength;
    }

    public Short getIsWait() {
        return isWait;
    }

    public void setIsWait(Short isWait) {
        this.isWait = isWait;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (!(obj instanceof Item)) {
//            return false;
//        }
//        Item other = (Item) obj;
//        if (this.isCompulsory && other.isCompulsory) {
//            return StringUtils.equals(this.getId(), other.getId());
//        } else if (this.getIndex() != null && other.getIndex() != null) {
//            //判断索引
//            return this.getIndex().equals(other.getIndex());
//        } else {
//            //老版本
//            if (!this.getKind().equals(other.getKind())) {
//                return false;
//            }
//            //普通菜
//            if (this.getKind() == CartEnums.ItemKindEnum.KIND_NORMAL.getKind()) {
//                return normalEquals(other);
//            } else if (this.getKind() == CartEnums.ItemKindEnum.KIND_SUIT.getKind()) {
//                //套餐
//                return suitEquals(other);
//            }
//        }
//        return false;
//    }
//
//    /*-----------------以下为封装的公共或私有方法------------------------*/
//
//    /**
//     * 更新item
//     *
//     * @param item item
//     */
//    public void update(Item item) {
//        this.setNum(item.getNum());
//        this.setAccountNum(item.getAccountNum());
//        this.setFee(item.getFee());
//        this.setMakeId(item.getMakeId());
//        this.setSpecDetailId(item.getSpecDetailId());
//        this.setSpecDetailName(item.getSpecDetailName());
//        this.setMakeName(item.getMakeName());
//        this.setChildItems(item.getChildItems());
//        this.setType(item.getType());
//
//        //如果是模板点餐,覆盖tag
//        if (item.getTag() > 0) {
//            this.setTag(item.getTag());
//        }
//    }
//
//    /**
//     * 判断两个普通菜是否是同一个菜
//     *
//     * @param other item
//     * @return 是否同一个菜
//     */
//    private boolean normalEquals(Item other) {
//        if (StringUtils.equals(this.getMenuId(), other.getMenuId()) && StringUtils.equals(this.getMakeId(), other.getMakeId())
//                && StringUtils.equals(this.getSpecDetailId(), other.getSpecDetailId())) {
//            List<Item> ownChild = this.childItems;
//            List<Item> otherChild = other.childItems;
//            if (CollectionUtils.isEmpty(ownChild) && CollectionUtils.isEmpty(otherChild)) {
//                return true;
//            } else if (CollectionUtils.isNotEmpty(ownChild) && CollectionUtils.isNotEmpty(otherChild)) {
//                if (ownChild.size() != otherChild.size()) {
//                    return false;
//                }
//                Map<String, Item> ownItemMap = Maps.uniqueIndex(ownChild, new Function<Item, String>() {
//                    @Override
//                    public String apply(Item input) {
//                        return input.getMenuId();
//                    }
//                });
//
//                for (Item item : otherChild) {
//                    Item getOne = ownItemMap.get(item.getMenuId());
//                    if (getOne == null) {
//                        return false;
//                    }
//                    if (!Objects.equals(getOne.getNum(), item.getNum())) {
//                        return false;
//                    }
//                }
//                return true;
//            } else {
//                //这里是一个孩子为空另外一个不为空的情况
//                return false;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 判断两个套餐是否是同一个菜
//     *
//     * @param other item
//     * @return 是否是同一个菜
//     */
//    private boolean suitEquals(Item other) {
//        List<Item> ownChild = this.childItems;
//        List<Item> otherChild = other.childItems;
//        if (CollectionUtils.isEmpty(ownChild) && CollectionUtils.isEmpty(otherChild)) {
//            return true;
//        }
//        if (ownChild.size() != otherChild.size()) {
//            return false;
//        }
//        Set<String> mine = new HashSet<>();
//        for (Item own : ownChild) {
//            mine.add(own.getMenuId() + "_" + own.getMakeId() + "_" + own.getSuitMenuDetailId() + "_" + own.getSpecDetailId() + "_" + own.getNum());
//        }
//        boolean flag = true;
//        for (Item otChild : otherChild) {
//            String key = otChild.getMenuId() + "_" + otChild.getMakeId() + "_" + otChild.getSuitMenuDetailId() + "_" + otChild.getSpecDetailId() + "_" + otChild.getNum();
//            if (!mine.contains(key)) {
//                flag = false;
//                return flag;
//            }
//        }
//        return flag;
//    }
}
