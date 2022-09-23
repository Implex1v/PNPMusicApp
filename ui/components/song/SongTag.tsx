import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTag, faXmark} from "@fortawesome/free-solid-svg-icons";

export default function SongTag({tag, disabled, onRemove }) {
    const key = `tag-${tag}`
    return (
        <span key={key} className="badge bg-success me-1">
            <FontAwesomeIcon icon={faTag} className="me-1" />
            {tag}
            {!disabled &&
                <a className="ms-1 text-light remove-tag" onClick={(e) => onRemove(key)}>
                    <FontAwesomeIcon icon={faXmark} className="me-1" />
                </a>
            }
        </span>
    )
}