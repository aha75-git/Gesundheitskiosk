type FeatureCardProps = {
    icon?:string;
    title?:string;
    description?:string;
    isFontawesome:boolean;
}

export default function FeatureCard(props: Readonly<FeatureCardProps>) {
    return (
        <div className="feature-card">
            <div className="feature-icon">
                {props.isFontawesome ? (
                        <i className={props.icon}></i>
                    ) : (
                        props.icon
                    )
                }
            </div>
            <h3>{props.title}</h3>
            <p>{props.description}</p>
        </div>
    )
}