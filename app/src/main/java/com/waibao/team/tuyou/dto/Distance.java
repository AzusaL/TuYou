package com.waibao.team.tuyou.dto;

import java.util.List;

/**
 * Created by Azusa on 2016/6/11.
 */
public class Distance {

    private int status;
    private String message;
    private InfoBean info;
    private ResultBean result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class InfoBean {

        private CopyrightBean copyright;

        public CopyrightBean getCopyright() {
            return copyright;
        }

        public void setCopyright(CopyrightBean copyright) {
            this.copyright = copyright;
        }

        public static class CopyrightBean {
            private String text;
            private String imageUrl;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }
    }

    public static class ResultBean {

        private List<ElementsBean> elements;

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public static class ElementsBean {
            /**
             * text : 1298.9公里
             * value : 1298937
             */

            private DistanceBean distance;
            /**
             * text : 18.1小时
             * value : 65262
             */

            private DurationBean duration;

            public DistanceBean getDistance() {
                return distance;
            }

            public void setDistance(DistanceBean distance) {
                this.distance = distance;
            }

            public DurationBean getDuration() {
                return duration;
            }

            public void setDuration(DurationBean duration) {
                this.duration = duration;
            }

            public static class DistanceBean {
                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class DurationBean {
                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
        }
    }
}
